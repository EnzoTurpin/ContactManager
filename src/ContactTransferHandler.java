import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

// Classe ContactTransferHandler pour gérer le glisser-déposer des contacts dans une liste
public class ContactTransferHandler extends TransferHandler {

    private final ContactChangeListener listener;

    // Constructeur prenant un écouteur de changement de contact
    public ContactTransferHandler(ContactChangeListener listener) {
        this.listener = listener;
    }

    // Crée un objet transférable à partir de la valeur sélectionnée dans la liste
    @Override
    protected Transferable createTransferable(JComponent c) {
        JList<?> sourceList = (JList<?>) c;
        Object selectedValue = sourceList.getSelectedValue();
        if (selectedValue instanceof Contact contact) {
            return new TransferableContact(contact);
        }
        return null;
    }

    // Définir les actions source autorisées (ici, déplacement)
    @Override
    public int getSourceActions(JComponent c) {
        return MOVE;
    }

    // Vérifie si le transfert de données peut être importé
    @Override
    public boolean canImport(TransferSupport support) {
        return support.isDataFlavorSupported(TransferableContact.CONTACT_FLAVOR);
    }

    // Importer les données transférées
    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        try {
            JList.DropLocation dl = (JList.DropLocation) support.getDropLocation();
            int index = dl.getIndex();
            JList<?> component = (JList<?>) support.getComponent();
            if (component.getModel() instanceof DefaultListModel<?>) {
                @SuppressWarnings("unchecked")
                DefaultListModel<Contact> listModel = (DefaultListModel<Contact>) component.getModel();
                Contact contact = (Contact) support.getTransferable().getTransferData(TransferableContact.CONTACT_FLAVOR);

                // Si le contact est déjà dans la liste, le déplacer
                if (listModel.contains(contact)) {
                    int currentIndex = listModel.indexOf(contact);
                    listModel.remove(currentIndex);
                    if (currentIndex < index) {
                        index--;
                    }
                }
                listModel.add(index, contact);
                component.setSelectedIndex(index);

                // Notifier l'écouteur de changement de liste
                if (listener != null) {
                    listener.onContactListChanged();
                }
                return true;
            }
        } catch (UnsupportedFlavorException | IOException ex) {
            Logger.getLogger(ContactTransferHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    // Interface pour écouter les changements de la liste de contacts
    public interface ContactChangeListener {
        void onContactListChanged();
    }

    // Classe interne pour représenter un contact transférable
    private record TransferableContact(Contact contact) implements Transferable {
        public static final DataFlavor CONTACT_FLAVOR = new DataFlavor(Contact.class, "Contact");

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{CONTACT_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return CONTACT_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return contact;
        }
    }
}
