import { Route, Routes } from "react-router";
import Home from "./pages/Home";
import PublishEvent from "./pages/PublishEvent";
import LatestMessages from "./pages/LatestMessages";



function App() {
  return (
    <>
      <Routes>
      <Route path="/" element={<Home />}>  </Route>
      <Route path="/publishEvent" element={<PublishEvent />}></Route>
      <Route path="/messages" element={<LatestMessages/>}></Route>
      </Routes>
    </>
  );
}

export default App;
