import { Button } from "react-bootstrap"
import SyntaxHighlighter from "react-syntax-highlighter";
import { dark } from "react-syntax-highlighter/dist/esm/styles/hljs";
import { useEffect, useState } from "react";
import { getTagByName } from "../../api/language-tags-api";
import { getSnippetById } from "../../api/snippets-api";
import type { LangTagEntity } from "../../model/tag";
import { useNavigate, useParams } from "react-router";
import type { SnippetEntity } from "../../model/snippet";
import LangTag from "../tags/LangTag";
import DeleteSnippetModal from "../modals/DeleteSnippetModal";
import { Toaster } from "react-hot-toast";

export default function SnippetView() {


    const { snippetId } = useParams();

    const [tag, setTag] = useState<LangTagEntity | null>(null);
    const [snippet, setSnippet] = useState<SnippetEntity | null>(null);
    const [showModal, setShowModal] = useState(false);

    const navigate = useNavigate();

    function toMainPage() {
        navigate(`/`);
    }

    function toEditView() {
        navigate(`/snippet/edit/${snippet?.id}`);
    }

    useEffect(() => {

        if (snippetId) {
            getSnippetById(snippetId).then(response => {
                if (response.data) {
                    getTagByName(response.data.language).then(response => {
                        if (response.data) {
                            setTag(response.data);
                        }
                    })
                    setSnippet(response.data);
                }
            });
        }
    }, [])

    return (
        <>
            <Toaster />
            <header className="snippet-header">
                <label className="application-label">Snippet Manager</label>
                <Button variant="primary" onClick={toMainPage}>Back to list</Button>
            </header>

            <main className="main-container">
                <section className="snippet-info">
                    <div className="snippet-view-group1">
                        <h1 className="snippet-title">{snippet?.title}</h1>
                        <LangTag color={tag?.color} language={snippet?.language} />
                    </div>
                    <div className="snippet-view-group2">
                        <Button variant="primary" onClick={toEditView}>Edit</Button>
                        <Button variant="danger" onClick={() => setShowModal(true)}>Delete</Button>
                    </div>
                </section>
                <section className="snippet-code-container">
                    <div className="snippet-code">
                        <button className="copy-btn">Copy</button>
                        <SyntaxHighlighter className="highlighter" language="java" style={dark}>
                            {snippet?.code ? snippet.code : ''}
                        </SyntaxHighlighter>
                    </div>
                    <div className="snippet-date-container">
                        <span className="snippet-date">Created: {new Date(snippet?.creationDate ? snippet.creationDate : '').toUTCString()}</span>
                    </div>
                </section>
            </main>

            <footer className="snippet-footer">
                <p>Snippet Manager © 2026</p>
            </footer>
            <DeleteSnippetModal snippet={snippet} showModal={showModal} setShowModal={setShowModal}
                closeModal={() => { setShowModal(false) }}></DeleteSnippetModal>
        </>
    );
}