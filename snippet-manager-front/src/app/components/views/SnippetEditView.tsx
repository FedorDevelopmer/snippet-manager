import { useEffect, useState } from "react";
import LangTagListItem from "../tags/LangTagListItem";
import type { LangTagEntity } from "../../model/tag";
import { useNavigate, useParams } from "react-router";
import { getTagByName, getTags } from "../../api/language-tags-api";
import { getSnippetById, updateSnippet } from "../../api/snippets-api";
import type { SnippetEntity } from "../../model/snippet";
import "../../../styles/editPage.css";
import { Button } from "react-bootstrap";

export default function SnippetEditView() {

    const [isOpen, setIsOpen] = useState(false);
    const [selected, setSelected] = useState<null | LangTagEntity>(null);
    const [tags, setTags] = useState<LangTagEntity[]>([]);
    const [snippet, setSnippet] = useState<SnippetEntity>({
        title: '',
        code: '',
        language: ''
    });

    const { snippetId } = useParams();

    const navigate = useNavigate();

    useEffect(() => {
        if (snippetId) {
            getSnippetById(snippetId).then((response) => {
                if (response.data) {
                    setSnippet(response.data);
                }
            });
        }
        getTags(0, 10).then((response) => {
            if (response.data.content) {
                setTags(response.data.content);
            }
        })
    }, []);

    useEffect(() => {
        if (snippet?.language) {
            getTagByName(snippet?.language).then((response) => {
                if (response.data) {
                    setSelected(response.data)
                }
            });
        }
    }, [snippet.language]);

    function onValueChange(e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) {
        const { name, value } = e.target;
        setSnippet(prev => ({
            ...prev,
            [name]: value
        }))
    }

    function handleUpdateSnippet(event: any) {
        event.preventDefault();
        console.log(snippet)
        updateSnippet(snippet).then((response) => {
            if (response.status == 200) {
                toSnippetPage();
            }
        })
    }

    function toSnippetPage() {
        navigate(`/snippet/${snippet.id}`)
    }


    return (
        <>
            <section>
                <h2 className="edit-page-header">Edit your snippet</h2>
                <form className="edit-page" onSubmit={handleUpdateSnippet}>
                    <label htmlFor="title">Title:</label>
                    <input id="title" name="title" value={snippet?.title} onChange={onValueChange}></input>
                    <label htmlFor="code">Code:</label>
                    <textarea id="code" name="code" value={snippet?.code} onChange={onValueChange}></textarea>
                    <label htmlFor="language">Language:</label>
                    <div id="language" className="language-selector">
                        <div
                            className="selector-button"
                            onClick={() => setIsOpen(prev => !prev)}
                        >
                            {selected ? <LangTagListItem color={selected?.color} language={selected?.language} /> : "Select language"}
                        </div>
                        {isOpen && (
                            <div className="dropdown">
                                {tags.map(tag => (
                                    <LangTagListItem key={tag.id} color={tag?.color} language={tag?.language} onClick={() => {
                                        setSelected(tag)
                                        setIsOpen(!isOpen)
                                        setSnippet((prev) => ({ ...prev, language: tag.language}));
                                    }} />
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="footer-buttons">
                        <Button variant="primary" type="submit">Save</Button>
                        <Button variant="danger" onClick={toSnippetPage}>Discard</Button>
                    </div>
                </form>

            </section>
        </>
    );
}