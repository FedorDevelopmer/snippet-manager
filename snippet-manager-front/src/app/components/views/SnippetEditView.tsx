import { useEffect, useState } from "react";
import LangTagListItem from "../tags/LangTagListItem";
import type { LangTagEntity } from "../../model/tag";
import { useNavigate, useParams } from "react-router";
import { getTagByName, getTags } from "../../api/language-tags-api";
import { getSnippetById, updateSnippet } from "../../api/snippets-api";
import type { SnippetEntity } from "../../model/snippet";
import "../../../styles/editPage.css";
import { Button } from "react-bootstrap";
import { zodResolver } from "@hookform/resolvers/zod";
import { ValidationSchema } from "../../validation/InputValidationSchema";
import { useForm } from "react-hook-form";
import { Toaster } from "react-hot-toast";

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

    const {
        register,
        handleSubmit,
        reset,
        formState: { errors }
    } = useForm({ resolver: zodResolver(ValidationSchema), });

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
        if (snippet) {
            reset({
                title: snippet.title,
                code: snippet.code
            });
        }
    }, [snippet, reset])

    useEffect(() => {
        if (snippet?.language) {
            getTagByName(snippet?.language).then((response) => {
                if (response.data) {
                    setSelected(response.data)
                }
            });
        }
    }, [snippet.language]);


    function handleUpdateSnippet(data: any) {
        const payload = {
            id: snippet.id,
            title: data.title,
            code: data.code,
            language: selected ? selected.language : snippet.language
        };

        updateSnippet(payload).then((response) => {
            if (response.status === 200) {
                toSnippetPage();
            }
        });
    }

    function toSnippetPage() {
        navigate(`/snippet/${snippet.id}`)
    }


    return (
        <>
            <Toaster />
            <section>
                <h2 className="edit-page-header">Edit your snippet</h2>
                <form className="edit-page" onSubmit={handleSubmit(handleUpdateSnippet)}>
                    <label htmlFor="title">Title:</label>
                    <input id="title"  {...register("title")}></input>
                    {errors.title && <label className="error">{errors.title.message}</label>}
                    <label htmlFor="code">Code:</label>
                    <textarea id="code"  {...register("code")}></textarea>
                    {errors.code && <label className="error">{errors.code.message}</label>}
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
                                        setSnippet((prev) => ({ ...prev, language: tag.language }));
                                    }} />
                                ))}
                            </div>
                        )}
                    </div>
                    <div className="footer-buttons">
                        <Button variant="primary" type="submit">Save</Button>
                        <Button variant="danger" type="button" onClick={toSnippetPage}>Discard</Button>
                    </div>
                </form>

            </section>
        </>
    );
}