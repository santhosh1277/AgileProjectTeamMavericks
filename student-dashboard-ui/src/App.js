import './App.css';
import Home from './HomePage/Home';
import { BrowserRouter,Route,Routes} from 'react-router-dom';

function App() {
  return (
    <BrowserRouter>
    <div className="App">
      <Routes>
        <Route path='/' element={<Home/>}/>
        //Add your component routes here for navigation
      </Routes>
    </div>
    </BrowserRouter>
  );
}

export default App;
