import { useNavigate } from 'react-router-dom';

function Home() {
  const navigate = useNavigate();

  return (
    <div className="container-fluid">
      <div className="row bg-dark">
        <div className="col-12 text-center">
          <h1 className="text-light text-align-center">Welcome to Student Dashboard</h1>
          <button
            className="btn btn-primary mt-3"
            onClick={() => navigate('/signup')}
          >
            Go to Signup
          </button>
        </div>
      </div>
    </div>
  );
}

export default Home;
