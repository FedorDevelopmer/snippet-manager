import { useEffect, useState } from "react";
import type { LangTagEntity } from "../model/tag";
import { getTags } from "../api/language-tags-api";
import { Button } from "react-bootstrap";
import { Toaster } from "react-hot-toast";

type SearchBarProps = {
    tagList: Set<string>;
    codeText: string;
    titleText: string;
    setTagList: (value: React.SetStateAction<Set<string>>) => void;
    setTitleText: (value: React.SetStateAction<string>) => void;
    setCodeText: (value: React.SetStateAction<string>) => void
}

export default function SearchBar(props: SearchBarProps) {

    const { tagList, codeText, titleText, setTagList, setTitleText, setCodeText } = props;


    const [tags, setTags] = useState<LangTagEntity[]>([]);
    const [totalElements, setTotalElements] = useState(5);
    const [showed, setShowed] = useState(false);

    useEffect(() => {
        getTags(0, 5).then((response) => {
            if (response.data.content) {
                setTags(response.data.content);
            }
            if (response.data.page.totalElements) {
                setTotalElements(response.data.page.totalElements);
            }
        })
    }, []);

    useEffect(() => {
        getTags(0, totalElements).then((response) => {
            if (response.data.content) {
                setTags(response.data.content);
            }
            if (!showed && response.data.page.totalElements) {
                setTotalElements(response.data.page.totalElements);
            }
        })
    }, [showed])

    const showMore = () => {
        if (showed) {
            setTotalElements(5);
            setShowed(false);
        } else {
            setShowed(true);
        }

    }



    return (

        <>
            <Toaster />
            <div className="tags-list">
                <label>Filter by language tag</label>
                {tags.map((tag) => (
                    <div className="tag-select">
                        <input type="checkbox"
                            id={tag.id}
                            name="subscribe"
                            value={tag.language}
                            onChange={(e) => {
                                const target = e.target as HTMLInputElement;
                                if (target.checked) {
                                    setTagList(prev => new Set(prev).add(tag.language));
                                } else {
                                    tagList.delete(tag.language)
                                    setTagList(prev => new Set(prev));
                                }
                            }}

                        ></input>
                        <label htmlFor={tag.id}>{tag.language}</label>
                    </div>
                ))}
                <Button onClick={showMore} hidden={showed} variant="primary">Show more...</Button>
                <Button onClick={showMore} hidden={!showed} variant="primary">Hide back</Button>
            </div>
            <label>Filter by title</label>
            <div className="search-bar">

                <input type="text"
                    placeholder="Enter title"
                    value={titleText}
                    onChange={(e) => {
                        setTitleText(e.target.value)

                    }}>
                </input>

            </div>
            <label>Filter by code</label>
            <div className="search-bar">

                <input type="text"
                    placeholder="Enter code"
                    value={codeText}
                    onChange={(e) => {
                        setCodeText(e.target.value)
                    }}>
                </input>

            </div>

        </>

    )
}