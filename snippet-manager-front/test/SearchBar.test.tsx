import { render, screen, waitFor } from "@testing-library/react";
import { describe, it, expect, vi, beforeEach } from "vitest";
import userEvent from "@testing-library/user-event";
import SearchBar from "../src/app/components/SearchBar";
import { useEffect, useState } from "react";
import { Criteria } from "../src/app/model/criteria";
import { getSnippets } from "../src/app/api/snippets-api";
import { getTags } from "../src/app/api/language-tags-api";
import { SnippetEntity } from "../src/app/model/snippet";



function SearchBarWrapper() {
    //entities states
    const [snippets, setSnippets] = useState<SnippetEntity[]>([]);

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

    useEffect(() => {
        getSnippets(currentPage, pageSize, criteria).then(response => {
            if (response.data.content) {
                setSnippets(response.data.content);
            }
            if (response.data.page) {
                setCurrentPage(response.data.page.number);
                setPagesCount(response.data.page.totalPages);
            }
        })
    }, [currentPage, pageSize, criteria]);

    return (
        <SearchBar tagList={tagList} codeText={codeText} titleText={titleText}
            setTagList={setTagList}
            setTitleText={setTitleText}
            setCodeText={setCodeText} ></SearchBar>
    )
}

vi.mock("../src/app/api/snippets-api", () => ({
    getSnippets: vi.fn()
}));

vi.mock("../src/app/api/language-tags-api", () => ({
    getTags: vi.fn()
}));

vi.mocked(getTags).mockResolvedValue({
    data: {
        content: [],
        page: {
            size: 15,
            number: 0,
            totalElements: 1,
            totalPages: 1,
        },
    }
} as any)


vi.mocked(getSnippets).mockImplementation((_, __, criteria) => {
    if (criteria.title === "2") {
        return Promise.resolve({
            data: {
                content: [
                    {
                        id: "2",
                        title: "My snippet 2",
                        code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                        language: "Java",
                        creationDate: "2026-01-01",
                    },
                ],

                page: {
                    size: 15,
                    number: 0,
                    totalElements: 1,
                    totalPages: 1,
                },
            }
        })
    } else {
        return Promise.resolve({
            data: {
                content: [
                    {
                        id: "1",
                        title: "My snippet 1",
                        code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                        language: "Text",
                        creationDate: "2026-01-01",
                    },
                    {
                        id: "2",
                        title: "My snippet 2",
                        code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                        language: "Java",
                        creationDate: "2026-01-01",
                    },
                ],

                page: {
                    size: 15,
                    number: 0,
                    totalElements: 2,
                    totalPages: 1,
                },
            }
        })
    }
});


describe("SearchBar", () => {

    it("check component render", async () => {

        render(<SearchBarWrapper />)

        //check elements existence
        const titleInput = await screen.findByLabelText("Filter by title")
        expect(titleInput).toBeInTheDocument();
        const codeInput = await screen.findByLabelText("Filter by code")
        expect(codeInput).toBeInTheDocument();
    });

    it("parameters input update", async () => {

        render(<SearchBarWrapper />);

        const user = userEvent.setup();
        const titleInput = await screen.findByLabelText("Filter by title");
        const codeInput = await screen.findByLabelText("Filter by code");

        // initial state
        expect(titleInput).toHaveValue("");
        expect(codeInput).toHaveValue("");

        // user interaction
        await user.type(titleInput, "java");
        await user.type(codeInput, "<script");

        // assert UI state update
        expect(titleInput).toHaveValue("java");
        expect(codeInput).toHaveValue("<script");
    });

    it("filtering criteria propagation", async () => {
        render(<SearchBarWrapper />)
        const user = userEvent.setup();
        
        //elements search
        const titleInput = await screen.findByLabelText("Filter by title")
        const codeInput = await screen.findByLabelText("Filter by code")

        //user interaction 
        await user.type(titleInput, "2");
        await user.type(codeInput, "<script");

        //check for call
        waitFor(() => {
            expect(
                vi.mocked(getSnippets).mock.calls.some(call =>
                    call[2].title === "2" &&
                    call[2].code === "<script"
                )
            ).toBe(true);
        })
    })
})