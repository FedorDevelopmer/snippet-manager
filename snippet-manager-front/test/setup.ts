import { afterEach, beforeAll } from 'vitest';
import { cleanup } from '@testing-library/react'
import '@testing-library/jest-dom'
import ReactModal from 'react-modal';

beforeAll(()=>{
    ReactModal.setAppElement(document.body);
})

afterEach(() => {
    cleanup();
})