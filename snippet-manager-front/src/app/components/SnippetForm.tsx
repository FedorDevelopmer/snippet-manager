import { useState } from 'react';
import ReactModal from 'react-modal';

export default function SnippetForm() {

    const [isOpen, setIsOpen] = useState(true);

    return (
        <div>
            <ReactModal isOpen={isOpen}>
                <p>Test how it works</p>
                <button onClick={() => setIsOpen(false)}>Close</button>
            </ReactModal>
        </div>
    )
}