import { createRoot } from 'react-dom/client'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css'
import App from './App.tsx'
import SnippetView from './app/components/views/SnippetView.tsx'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import ReactModal from 'react-modal'
import SnippetEditView from './app/components/views/SnippetEditView.tsx'
import OfflineViewComponent from './app/components/views/OfflineView.tsx'
import MainMenu from './app/components/MainMenu.tsx'



createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <Routes>
      <Route path="/" element={<App />} />
      <Route path="/main" element={<MainMenu />} />
      <Route path="/snippet/:snippetId" element={<SnippetView />} />
      <Route path="/snippet/edit/:snippetId" element={<SnippetEditView />} />
      <Route path="/offline" element={<OfflineViewComponent />} />
    </Routes>
  </BrowserRouter>
)
ReactModal.setAppElement('#root');
