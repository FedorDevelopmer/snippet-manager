import { Button } from "react-bootstrap";
import { useNavigate } from "react-router";
import { Toaster } from "react-hot-toast";
import Offline from '../../../assets/offline.svg?react';

export default function OfflineViewComponent() {

    const navigate = useNavigate();

    function onRetry() {
        navigate("/");
    }

    return (
        <>
            <Toaster />
            <section className="offlinePageLayout">
                <Offline className="offlineImg"></Offline>
                <label className="offlineLabel">Unable to connect to application server. Please, try againg later.</label>
                <Button variant="outline-warning" onClick={onRetry}>Try again</Button>
            </section>
        </>
    );
}