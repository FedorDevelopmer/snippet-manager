import { Button } from "react-bootstrap";
import noConnection from '../../../assets/offline.svg';
import { useNavigate } from "react-router";
import { Toaster } from "react-hot-toast";

export default function OfflineViewComponent() {

    const navigate = useNavigate();

    function onRetry() {
        navigate("/");
    }

    return (
        <>
           {/* <Toaster />  */}
            <section className="offlinePageLayout">
                <img className="offlineImg" src={noConnection}></img>
                <label className="offlineLabel">Unable to connect to application server. Please, try againg later.</label>
                <Button variant="primary" onClick={()=> {navigate("/")}}>Try again</Button>
            </section>
        </>
    );
}