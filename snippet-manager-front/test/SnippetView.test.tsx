
import { beforeEach, describe, expect, it, vi } from "vitest";
import { render, within } from "@testing-library/react";
import { screen } from "@testing-library/react";
import { getSnippetById } from "../src/app/api/snippets-api";
import SnippetView from "../src/app/components/views/SnippetView"
import { MemoryRouter, Route, Routes } from "react-router";
import userEvent from "@testing-library/user-event";
import { addWeeks } from "date-fns";


async function findModal() {
    return screen.findByRole("dialog", {
        hidden: true
    });
}

vi.mock("../src/app/api/snippets-api", () => (
    {
        getSnippetById: vi.fn()
    }
));

vi.mocked(getSnippetById).mockImplementation(() => {
    return Promise.resolve({
        data: {
            content: [
                {
                    id: "1",
                    title: "My snippet 1",
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
    }) as any;
});

describe("SnippetPage", () => {


    beforeEach(() => {
        vi.clearAllMocks();
        
    });

    it("check component render", async () => {

        render(
            <MemoryRouter initialEntries={["/snippet/1"]}>
                <Routes>
                    <Route path="/snippet/:snippetId" element={<SnippetView />} />
                </Routes>
            </MemoryRouter>
        )  
        const codeContainer = await screen.findByTitle("Code section");
        expect(codeContainer).toBeInTheDocument();

        const editButton = await screen.findByTitle("Edit button");
        expect(editButton).toBeInTheDocument();

        const deleteButton = await screen.findByTitle("Delete button");
        expect(deleteButton).toBeInTheDocument();
    });


    it("should open delete modal after clicking delete button", async () => {

        render(
            <MemoryRouter initialEntries={["/snippet/1"]}>
                <SnippetView />
            </MemoryRouter>
        );

        const deleteButton = await screen.findByTitle("Delete button");

        await userEvent.click(deleteButton);

        const dialog = await findModal();

        expect(
            await within(dialog).findByText("Are you sure to delete this snippet?")
        ).toBeInTheDocument();

        expect(
            within(dialog).getByText("This action is impossible to revert!")
        ).toBeInTheDocument();

        expect(
            within(dialog).getByRole("button", { name: "Delete", hidden: true })
        ).toBeInTheDocument();

        expect(
            within(dialog).getByRole("button", { name: "Cancel", hidden: true })
        ).toBeInTheDocument();
    });
})