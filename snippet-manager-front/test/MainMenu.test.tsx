import { render, screen } from "@testing-library/react";
import { describe, it, expect, vi, beforeEach } from "vitest";
import { MemoryRouter } from "react-router-dom";

import MainMenu from "../src/app/components/MainMenu";

import * as snippetsApi from "../src/app/api/snippets-api";
import * as tagsApi from "../src/app/api/language-tags-api";
import userEvent from "@testing-library/user-event";


vi.mock("../src/app/api/snippets-api", () => ({
    getSnippets: vi.fn(),
}));

vi.mock("../src/app/api/language-tags-api", () => ({
    getTagByName: vi.fn(),
    getTags: vi.fn(),
}));

vi.mock("../src/app/components/AddButtonComponent", () => ({
    default: () => <div />,
}));

vi.mock("../src/app/components/SearchBar", () => ({
    default: () => <div />,
}));

describe("Main menu", () => {

    beforeEach(() => {
        vi.clearAllMocks();
    });

    it("renders snippets after API load", async () => {

        vi.mocked(snippetsApi.getSnippets).mockResolvedValue({
            data: {
                content: [
                    {
                        id: "1",
                        title: "My snippet",
                        code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                        language: "Text",
                        creationDate: "2026-01-01",
                    },
                ],
                page: {
                    size: 15,
                    number: 0,
                    totalElements: 1,
                    totalPages: 1,
                },
            },
        } as any);

        vi.mocked(tagsApi.getTagByName).mockResolvedValue({
            data: {
                name: "Text",
                color: "#ff0000",
            },
        } as any);

        vi.mocked(tagsApi.getTags).mockResolvedValue({
            data: {
                content: [],
            },
        } as any);

        render(
            <MemoryRouter>
                <MainMenu />
            </MemoryRouter>
        );

        const snippet = await screen.findByText("My snippet");

        expect(snippet).toBeInTheDocument();
    });

    it("loads next page when user clicks pagination", async () => {

        const user = userEvent.setup();

        vi.mocked(snippetsApi.getSnippets).mockImplementation((page: any) => {

            if (page === 0 || page.pageNumber === 0) {
                return Promise.resolve({
                    data: {
                        content: [
                            {
                                id: "1",
                                title: "Page 1 snippet",
                                code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                                language: "Text",
                                creationDate: "2026-01-01",
                            },
                        ],
                        page: {
                            size: 15,
                            number: 0,
                            totalElements: 2,
                            totalPages: 2,
                        },
                    },
                } as any);
            }

            return Promise.resolve({
                data: {
                    content: [
                        {
                            id: "2",
                            title: "Page 2 snippet",
                            code: "<script src=/_/docs/_/js/k=docs.client_js_prod.ru.yCsmxVLeaL8.es5.O/am=EEDgAIAB/d=0/wt=0/rs=AGWKN8nZloZvV3GXo7FoDo0XRBI1R08-4Q/m=kix_core id=base-js nonce=></script>",
                            language: "Text",
                            creationDate: "2026-01-02",
                        },
                    ],
                    page: {
                        size: 15,
                        number: 1,
                        totalElements: 2,
                        totalPages: 2,
                    },
                },
            } as any);
        });

        vi.mocked(tagsApi.getTagByName).mockResolvedValue({
            data: {
                name: "Text",
                color: "#ff0000",
            },
        } as any);

        vi.mocked(tagsApi.getTags).mockResolvedValue({
            data: { content: [] },
        } as any);

        render(
            <MemoryRouter>
                <MainMenu />
            </MemoryRouter>
        );

        expect(await screen.findByText("Page 1 snippet")).toBeInTheDocument();

        const nextButton = screen.getByText(/next/i);

        await user.click(nextButton);

        expect(await screen.findByText("Page 2 snippet")).toBeInTheDocument();
    });
});