import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { MemoryRouter, Route, Routes } from "react-router";

import SnippetEditView from "../src/app/components/views/SnippetEditView";

import {
    getSnippetById,
    updateSnippet
} from "../src/app/api/snippets-api";

import {
    getTags,
    getTagByName
} from "../src/app/api/language-tags-api";

const mockNavigate = vi.fn();

vi.mock("react-router", async () => {
    const actual = await vi.importActual("react-router");

    return {
        ...actual,
        useNavigate: () => mockNavigate
    };
});

vi.mock("../src/app/api/snippets-api", () => ({
    getSnippetById: vi.fn(),
    updateSnippet: vi.fn()
}));

vi.mock("../src/app/api/language-tags-api", () => ({
    getTags: vi.fn(),
    getTagByName: vi.fn()
}));

describe("SnippetEditView", () => {

    beforeEach(() => {
        vi.clearAllMocks();

        vi.mocked(getSnippetById).mockResolvedValue({
            data: {
                id: "1",
                title: "My snippet 1",
                code: "<script>alert('test')</script>",
                language: "Java",
                creationDate: "2026-01-01"
            }
        } as any);

        vi.mocked(getTagByName).mockResolvedValue({
            data: {
                id: "1",
                language: "Java",
                color: "#ff0000"
            }
        } as any);

        vi.mocked(getTags).mockResolvedValue({
            data: {
                content: [
                    {
                        id: "1",
                        language: "Java",
                        color: "#ff0000"
                    },
                    {
                        id: "2",
                        language: "Python",
                        color: "#00ff00"
                    },
                    {
                        id: "3",
                        language: "JavaScript",
                        color: "#ffff00"
                    }
                ]
            }
        } as any);

        vi.mocked(updateSnippet).mockResolvedValue({
            status: 200
        } as any);
    });

    function renderView() {
        return render(
            <MemoryRouter initialEntries={["/snippet/edit/1"]}>
                <Routes>
                    <Route
                        path="/snippet/edit/:snippetId"
                        element={<SnippetEditView />}
                    />
                </Routes>
            </MemoryRouter>
        );
    }

    it("should render edit form", async () => {
        renderView();

        expect(
            await screen.findByText("Edit your snippet")
        ).toBeInTheDocument();

        expect(screen.getByLabelText("Title")).toBeInTheDocument();
        expect(screen.getByLabelText("Code")).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: "Save" })
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: "Discard" })
        ).toBeInTheDocument();
    });

    it("should populate form with snippet data", async () => {
        renderView();

        expect(
            await screen.findByDisplayValue("My snippet 1")
        ).toBeInTheDocument();

        expect(
            screen.getByDisplayValue("<script>alert('test')</script>")
        ).toBeInTheDocument();
    });

    it("should open language dropdown", async () => {
        renderView();

        const languageSelector =
            await screen.findByText("Java");

        await userEvent.click(languageSelector);

        expect(
            await screen.findByText("Python")
        ).toBeInTheDocument();

        expect(
            screen.getByText("JavaScript")
        ).toBeInTheDocument();
    });

    it("should submit updated snippet", async () => {
        renderView();

        const titleInput =
            await screen.findByDisplayValue("My snippet 1");

        await userEvent.clear(titleInput);
        await userEvent.type(titleInput, "Updated snippet");

        await userEvent.click(
            screen.getByRole("button", { name: "Save" })
        );

        expect(updateSnippet).toHaveBeenCalledWith({
            id: "1",
            title: "Updated snippet",
            code: "<script>alert('test')</script>",
            language: "Java"
        });
    });
});