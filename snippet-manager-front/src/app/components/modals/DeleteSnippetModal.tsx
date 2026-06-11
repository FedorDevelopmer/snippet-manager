import { Button, CloseButton, Modal } from "react-bootstrap";
import ReactModal from "react-modal";
import { deleteSnippet } from "../../api/snippets-api";
import { useNavigate } from "react-router";

export default function DeleteSnippetModal(props: any) {

    const { snippet, showModal, setShowModal, closeModal } = props;

    const navigate = useNavigate();


    function handleDeleteSnippet() {
        deleteSnippet(snippet).then((response) => {
            if (response.status == 204) {
                closeModal();
                navigate(`/`);
            }
        })
    }

    return (
        <ReactModal className="custom-modal" 
        overlayClassName="custom-modal-overlay" 
        isOpen={showModal} 
        onRequestClose={() => setShowModal(false)}>
            <Modal.Header>
                <Modal.Title>Are you sure to delete this snippet?</Modal.Title>
                <CloseButton onClick={closeModal} variant="white"></CloseButton>
            </Modal.Header>
            <Modal.Body>
                <label>This action is impossible to revert!</label>
            </Modal.Body>
            <Modal.Footer>
                <div style={{display:"inline-flex", gap: "1rem"}}>
                    <Button variant="outline-danger" onClick={handleDeleteSnippet}>Delete</Button>
                    <Button variant="outline-secondary" onClick={closeModal}>Cancel</Button>
                </div>
            </Modal.Footer>
        </ReactModal>
    )
}