import { Button, Card, CloseButton, Modal } from "react-bootstrap";
import '../../../src/styles/mainPage.css';
import type { SnippetEntity } from "../model/snippet";
import { addSnippet } from "../api/snippets-api";
import { useEffect, useState } from "react";
import { getTags } from "../api/language-tags-api";
import LangTagListItem from "./tags/LangTagListItem";
import type { LangTagEntity } from "../model/tag";
import ReactModal from "react-modal";

export default function AddButtonComponent(props: any) {

    const [showModal, setShowModal] = useState(false);
    const [tags, setTags] = useState<LangTagEntity[]>([]);
    const [isOpen, setIsOpen] = useState(false);
    const [selected, setSelected] = useState<null | LangTagEntity>(null);
    const [snippetValues, setSnippetValues] = useState<SnippetEntity>({
        title: '',
        code: '',
        language: ''
    });

    const { onModalClose } = props;

    useEffect(() => {
        getTags(0, 10).then(response => {
            if (response.data.content) {
                setTags(response.data.content);
            }
        });
    }, [])


    function onValueChange(e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = e.target;
        setSnippetValues(prev => ({
            ...prev,
            [name]: value
        }))
    }

    function hideModal() {
        setShowModal(false);
    }

    function openModal() {
        setShowModal(true);
    }

    function createSnippet(event: any) {
        event.preventDefault();
        let snippetEntity: SnippetEntity = {
            title: snippetValues.title,
            code: snippetValues.code,
            language: selected?.language ? selected.language : 'Text',
        }
        addSnippet(snippetEntity).then(() => {
            hideModal();
            onModalClose();
        }, () => {
            hideModal();
        })

    }



    return (
        <>
            <Card style={{ width: '18rem' }}>
                <Card.Body style={{ display: "flex", alignItems: "center", justifyContent: "center" }}>
                    <Button variant="outline-primary" className="add-button" onClick={openModal}>+</Button>
                </Card.Body>
            </Card>
            <ReactModal className="modal" overlayClassName="modal-overlay" style={{ content: {}, overlay: {} }}isOpen={showModal} onRequestClose={() => setShowModal(false)}
                shouldCloseOnOverlayClick={true}
                shouldCloseOnEsc={true}>
                <Modal.Header>
                    <Modal.Title>Create new code snippet</Modal.Title>
                    <CloseButton onClick={hideModal} />
                </Modal.Header>
                <form onSubmit={(e) => {
                    setShowModal(false);
                    createSnippet(e);
                    onModalClose();
                }}>
                    <Modal.Body>
                        <div className="modal-form">
                            <label htmlFor="title">Title</label>
                            <input type="text" id="title" name="title" value={snippetValues.title} onChange={onValueChange}></input>
                            <label htmlFor="code">Code</label>
                            <textarea id="code" name="code" className="textArea" value={snippetValues.code} onChange={onValueChange}></textarea>
                            <label htmlFor="lang">Language</label>
                            <div className="language-selector">
                                <div
                                    className="selector-button"
                                    onClick={() => setIsOpen(prev => !prev)}
                                >
                                    {selected ? <LangTagListItem color={selected?.color} language={selected?.language} /> : "Select language"}
                                </div>

                                {isOpen && (
                                    <div className="dropdown">
                                        {tags.map(tag => (
                                            <LangTagListItem color={tag?.color} language={tag?.language} onClick={() => {
                                                setSelected(tag)
                                                setIsOpen(!isOpen)
                                            }} />
                                        ))}
                                    </div>
                                )}
                            </div>
                        </div>
                    </Modal.Body>
                    <Modal.Footer>
                        <Button variant="secondary" onClick={hideModal}>Cancel</Button>
                        <Button variant="primary" type="submit">Create</Button>
                    </Modal.Footer>
                </form>
            </ReactModal>
        </>
    );
}