import { Route, Routes } from 'react-router-dom';
import Home from './pages/Home';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import NotFound from './pages/NotFound';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="*" element={<NotFound/>} />
      {/*<Route path="/write" element={<WritePage />} />*/}
      {/*<Route path="/@:username">*/}
      {/*  <Route index element={<PostListPage />} />*/}
      {/*  <Route path=":postId" element={<PostPage />} />*/}
      {/*</Route>*/}
    </Routes>
  );
};
export default App;