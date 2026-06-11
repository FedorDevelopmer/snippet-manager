import { Button } from "react-bootstrap"
import SyntaxHighlighter from "react-syntax-highlighter";
import { dark } from "react-syntax-highlighter/dist/esm/styles/hljs";
import { useEffect, useState } from "react";
import { getSnippetById } from "../../api/snippets-api";
import { useNavigate, useParams } from "react-router";
import type { SnippetEntity } from "../../model/snippet";
import DeleteSnippetModal from "../modals/DeleteSnippetModal";
import toast, { Toaster } from "react-hot-toast";
import Edit from '../../../assets/edit.svg?react';
import Delete from '../../../assets/delete.svg?react';
import Code from '../../../assets/code.svg?react';
import Copy from '../../../assets/copy.svg?react';
import { format } from 'date-fns';

export default function SnippetView() {


    const { snippetId } = useParams();
    const [snippet, setSnippet] = useState<SnippetEntity | null>(null);
    const [showModal, setShowModal] = useState(false);

    const navigate = useNavigate();

    function toMainPage() {
        navigate(`/`);
    }

    function toEditView() {
        navigate(`/snippet/edit/${snippet?.id}`);
    }

    function copyCode() {
        const snippetCode: string = snippet?.code ? snippet.code : '';
        navigator.clipboard.writeText(snippetCode);
        toast.success("Copied to clipboard!");

    }

    useEffect(() => {
        if (snippetId) {
            getSnippetById(snippetId).then(response => {
                if (response.data) {
                    setSnippet(response.data);
                }
            });
        }
    }, [])

    return (
        <>
            <Toaster />
            <section className="main-container">
                <section className="snippet-info">
                    <h1 className="snippet-title">{snippet?.title}</h1>
                    <div className="snippet-view-group">
                        <Edit className="edit-icon" onClick={toEditView} title="Edit button" />
                        <Delete className="delete-icon" onClick={() => setShowModal(true)} title="Delete button"/>
                        <Button variant="outline-warning" onClick={toMainPage}>Back to list</Button>
                    </div>
                </section>
                <section className="snippet-code-container">
                    <div className="snippet-top">
                        <Code className="code-icon" />
                        <label>{snippet?.language}</label>
                    </div>
                    <div className="snippet-code">
                        <Copy className="copy-btn" onClick={copyCode} />
                        <SyntaxHighlighter title="Code section" className="highlighter" language="java" style={dark}>
                            {snippet?.code ? snippet.code : ''}
                        </SyntaxHighlighter>
                    </div>
                    <div className="snippet-bottom">
                        <span className="snippet-date">{format(new Date(snippet?.creationDate ? snippet.creationDate : 0), 'dd MMM, yyyy HH:mm:ss')}</span>
                    </div>
                </section>
            </section >
            <DeleteSnippetModal snippet={snippet} showModal={showModal} setShowModal={setShowModal}
                closeModal={() => { setShowModal(false) }}></DeleteSnippetModal>
        </>
    );
}