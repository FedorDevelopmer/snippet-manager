import { Button, ButtonGroup, Card, Pagination } from "react-bootstrap";
import AddButtonComponent from "./AddButtonComponent";
import type { SnippetEntity } from "../model/snippet";
import { useCallback, useEffect, useState } from "react";
import { getSnippets } from "../api/snippets-api";
import type { LangTagEntity } from "../model/tag";
import { getTagByName } from "../api/language-tags-api";
import LangTag from "./tags/LangTag";
import { useNavigate } from "react-router-dom";
import SearchBar from "./SearchBar";
import type { Criteria } from "../model/criteria";
import { Toaster } from "react-hot-toast";
import LoadingOverlay from 'react-loading-overlay-ts';

export default function MainMenu() {

    //entities states
    const [snippets, setSnippets] = useState<SnippetEntity[]>([]);
    const [tags, setTags] = useState<LangTagEntity[]>([]);

    //pagination states
    const [currentPage, setCurrentPage] = useState(0);
    const [pageSize, setPageSize] = useState(15);
    const [pagesCount, setPagesCount] = useState(0);

    //search states(managed by SearchBar component)
    const [tagList, setTagList] = useState<Set<string>>(new Set());
    const [titleText, setTitleText] = useState("");
    const [codeText, setCodeText] = useState("");
    const [criteria, setCriteria] = useState<Criteria>(
        {
            tags: null,
            code: null,
            title: null,
        }
    );

    //loading states
    const [isLoading, setIsLoading] = useState(true);

    const navigate = useNavigate();
    useEffect(() => {


        getSnippets(0, pageSize, criteria).then(response => {
            if (response.data.content) {
                setSnippets(response.data.content);
            }
            if (response.data.page) {
                setCurrentPage(response.data.page.number);
                setPagesCount(response.data.page.totalPages);
            }
        })

    }, []);

    useEffect(() => {
        setIsLoading(true);
        getSnippets(currentPage, pageSize, criteria).then(response => {
            if (response.data.content) {
                setSnippets(response.data.content);
            }
            if (response.data.page) {
                setCurrentPage(response.data.page.number);
                setPagesCount(response.data.page.totalPages);
            }
            setIsLoading(false);
        }, () => {
            setIsLoading(false);
        })
    }, [currentPage, pageSize, criteria]);

    useEffect(() => {
        for (let snippet of snippets) {
            getTagByName(snippet.language).then((response) => {
                if (response.data) {
                    setTags(prev => [response.data, ...prev])
                }
            })
        }
    }, [snippets])

    const onModalClose = useCallback(() => {
        getSnippets(currentPage, pageSize, criteria).then(response => {
            if (response.data.content) {
                setSnippets(response.data.content);
            }
            if (response.data.page) {
                setCurrentPage(response.data.page.number);
                setPagesCount(response.data.page.totalPages);
            }
        })
    }, []);

    useEffect(() => {
        if (titleText != "") {
            setCriteria(prev => ({ ...prev, title: titleText }))
        } else {
            setCriteria(prev => ({ ...prev, title: null }))
        }
        if (codeText != "") {
            setCriteria(prev => ({ ...prev, code: codeText }))
        } else {
            setCriteria(prev => ({ ...prev, code: null }))
        }
        if (tagList.size != 0) {
            setCriteria(prev => ({ ...prev, tags: tagList }))
        } else {
            setCriteria(prev => ({ ...prev, tags: null }))
        }

    }, [titleText, codeText, tagList])

    return (
        <>
            <Toaster />
            <LoadingOverlay
                className="overlay"
                active={isLoading}
                spinner
                text='Loading...'
            >
                <div className="root-element">
                    <section className="side-menu">
                        <SearchBar
                            tagList={tagList}
                            setTagList={setTagList}
                            codeText={codeText}
                            setCodeText={setCodeText}
                            titleText={titleText}
                            setTitleText={setTitleText} />
                    </section>

                    <section className="main-section">
                        <section className="page-size-selection">
                            <label>Choose page size:</label>
                            <ButtonGroup>
                                <Button variant={pageSize == 7 ? "warning" : "outline-warning"}
                                    onClick={() => {
                                        setPageSize(7);
                                        setCurrentPage(0);
                                    }}>7</Button>
                                <Button variant={pageSize == 15 ? "warning" : "outline-warning"}
                                    onClick={() => {
                                        setPageSize(15);
                                        setCurrentPage(0);
                                    }}>15</Button>
                                <Button variant={pageSize == 31 ? "warning" : "outline-warning"}
                                    onClick={() => {
                                        setPageSize(31);
                                        setCurrentPage(0);
                                    }}>31</Button>
                            </ButtonGroup>
                        </section>
                        <section className="main-menu">
                            <AddButtonComponent onModalClose={onModalClose} />
                            {snippets.map((snippet, idx) => (
                                <Card className="custom-card" key={snippet.id}>
                                    <Card.Body>
                                        <Card.Title>{snippet.title}</Card.Title>
                                        <Card.Body>
                                            <LangTag key={idx} color={tags[idx] ? tags[idx].color : "#000000"} language={snippet.language} />
                                        </Card.Body>
                                        <Card.Text>
                                            Issued at: {new Date(snippet?.creationDate ? snippet.creationDate : '').toUTCString()}
                                        </Card.Text>
                                        <Button variant="outline-warning" onClick={() => {
                                            navigate(`/snippet/${snippet.id}`);
                                        }}>Open</Button>
                                    </Card.Body>
                                </Card>
                            ))}
                        </section>
                        <section className="pagination">
                            <Pagination >
                                <Pagination.First disabled={currentPage <= 0} onClick={() => { setCurrentPage(0) }}></Pagination.First>
                                <Pagination.Prev disabled={currentPage <= 0} onClick={() => { setCurrentPage(currentPage - 1) }}></Pagination.Prev>
                                {(() => {
                                    const pageButtons = [];
                                    pageButtons.push(
                                        <Pagination.Item
                                            active={currentPage == 0}
                                            onClick={() => {
                                                setCurrentPage(0);
                                            }}>
                                            {1}
                                        </Pagination.Item>
                                    );
                                    for (let i = 1; i < pagesCount; i++) {
                                        pageButtons.push(
                                            <Pagination.Item
                                                active={currentPage == i}
                                                onClick={() => {
                                                    setCurrentPage(i);
                                                }}>
                                                {i + 1}
                                            </Pagination.Item>
                                        );
                                    }
                                    return pageButtons;
                                })()}
                                <Pagination.Next disabled={currentPage >= pagesCount - 1} onClick={() => { setCurrentPage(currentPage + 1) }}></Pagination.Next>
                                <Pagination.Last disabled={currentPage >= pagesCount - 1} onClick={() => { setCurrentPage(pagesCount - 1) }}></Pagination.Last>
                            </Pagination>
                        </section>
                    </section>
                </div >
            </LoadingOverlay>
        </>

    );
}