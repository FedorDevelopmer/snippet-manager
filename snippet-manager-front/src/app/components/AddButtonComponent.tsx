import { Button, Card, CloseButton, Modal } from "react-bootstrap";
import '../../../src/styles/mainPage.css';
import type { SnippetEntity } from "../model/snippet";
import { addSnippet } from "../api/snippets-api";
import { useEffect, useState } from "react";
import { getTags } from "../api/language-tags-api";
import LangTagListItem from "./tags/LangTagListItem";
import type { LangTagEntity } from "../model/tag";
import ReactModal from "react-modal";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ValidationSchema } from "../validation/InputValidationSchema";
import { Toaster } from "react-hot-toast";

export default function AddButtonComponent(props: any) {

    const [showModal, setShowModal] = useState(false);
    const [tags, setTags] = useState<LangTagEntity[]>([]);
    const [isOpen, setIsOpen] = useState(false);
    const [selected, setSelected] = useState<null | LangTagEntity>(null);

    const {
        register,
        handleSubmit,
        formState: { errors }
    } = useForm({ resolver: zodResolver(ValidationSchema), });

    const { onModalClose } = props;

    useEffect(() => {
        getTags(0, 10).then(response => {
            if (response.data.content) {
                setTags(response.data.content);
            }
        });
    }, []);

    function hideModal() {
        setShowModal(false);
    }

    function openModal() {
        setShowModal(true);
    }

    function createSnippet(data: any) {
        let snippetEntity: SnippetEntity = {
            title: data.title,
            code: data.code,
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
            <Toaster />
            <Card style={{ background: "#553939" }}>
                        <span className="add-button" onClick={openModal}>+</span>
            </Card>
            <ReactModal className="custom-modal" overlayClassName="custom-modal-overlay" style={{ content: {}, overlay: {} }} isOpen={showModal} onRequestClose={() => setShowModal(false)}
                shouldCloseOnOverlayClick={true}
                shouldCloseOnEsc={true}>
                <Modal.Header>
                    <Modal.Title>Create new code snippet</Modal.Title>
                    <CloseButton variant="white" onClick={hideModal} />
                </Modal.Header>
                <form onSubmit={handleSubmit(createSnippet)}>
                    <Modal.Body>
                        <div className="modal-form">
                            <label htmlFor="title">Title</label>
                            <input type="text" id="title" {...register("title")}></input>
                            {errors.title && <label className="error">{errors.title.message}</label>}
                            <label htmlFor="code">Code</label>
                            <textarea id="code" {...register("code")}></textarea>
                            {errors.code && <label className="error">{errors.code.message}</label>}
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
                        <Button variant="outline-danger" onClick={hideModal}>Cancel</Button>
                        <Button variant="outline-warning" type="submit">Create</Button>
                    </Modal.Footer>
                </form>
            </ReactModal >
        </>
    );
}