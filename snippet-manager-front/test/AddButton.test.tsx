import { render, screen } from "@testing-library/react";
import { describe, expect, it, vi } from "vitest";
import AddButtonComponent from "../src/app/components/AddButtonComponent";
import userEvent from "@testing-library/user-event";
import { getTags } from "../src/app/api/language-tags-api";
import { addSnippet } from "../src/app/api/snippets-api";

vi.mock("../src/app/api/language-tags-api", () => ({
    getTags: vi.fn(),
}));

vi.mock("../src/app/api/snippets-api", () => ({
    addSnippet: vi.fn(),
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

describe("AddButtonComponent", () => {

    it("opens modal on button click", async () => {

        render(<AddButtonComponent onModalClose={vi.fn()} />);

        const user = userEvent.setup();

        const openBtn = screen.getByText("+");

        await user.click(openBtn);

        expect(
            screen.getByText("Create new code snippet")
        ).toBeInTheDocument();
    });

    it("shows validation errors when submitting empty form", async () => {

        render(<AddButtonComponent onModalClose={vi.fn()} />);

        const user = userEvent.setup();

        await user.click(screen.getByText("+"));

        await user.click(screen.getByRole("button", { name: /create/i, hidden: true}));

        expect(await screen.findByText("Code length must be at least 10 characters")).toBeInTheDocument();
        expect(await screen.findByText("Snippet title must be at list 3 characters")).toBeInTheDocument();
    });

    it("creates snippet successfully", async () => {

        const onModalClose = vi.fn();

        vi.mocked(addSnippet).mockResolvedValue({} as any);

        render(<AddButtonComponent onModalClose={onModalClose} />);

        const user = userEvent.setup();

        await user.click(screen.getByText("+"));

        await user.type(screen.getByLabelText("Title"), "My snippet");
        await user.type(screen.getByLabelText("Code"), "console.log(10)");

        await user.click(screen.getByRole("button", { name: /create/i, hidden: true}));

        expect(addSnippet).toHaveBeenCalledWith(
            expect.objectContaining({
                title: "My snippet",
                code: "console.log(10)",
                language: "Text"
            })
        );



        expect(onModalClose).toHaveBeenCalled();
    });
})