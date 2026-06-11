import { render, screen } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { vi, describe, it, expect } from "vitest";
import OfflineViewComponent from "../src/app/components/views/OfflineView";

const mockNavigate = vi.fn();

vi.mock("react-router", async () => {
    const actual = await vi.importActual("react-router");

    return {
        ...actual,
        useNavigate: () => mockNavigate
    };
});

describe("OfflineViewComponent", () => {

    it("should render offline page", () => {
        render(<OfflineViewComponent />);

        expect(
            screen.getByText(
                "Unable to connect to application server. Please, try againg later."
            )
        ).toBeInTheDocument();

        expect(
            screen.getByRole("button", { name: "Try again" })
        ).toBeInTheDocument();
    });

    it("should navigate to main page after retry click", async () => {

        render(<OfflineViewComponent />);

        await userEvent.click(
            screen.getByRole("button", { name: "Try again" })
        );

        expect(mockNavigate).toHaveBeenCalledWith("/");
    });
});