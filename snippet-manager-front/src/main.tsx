import { createRoot } from 'react-dom/client'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import App from './App.tsx'
import SnippetView from './app/components/views/SnippetView.tsx'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import ReactModal from 'react-modal'
import SnippetEditView from './app/components/views/SnippetEditView.tsx'


ReactModal.setAppElement('#root');
createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/snippet/:snippetId" element={<SnippetView />} />
      <Route path="/snippet/edit/:snippetId" element={<SnippetEditView/>} />
    </Routes>
  </BrowserRouter>
)
