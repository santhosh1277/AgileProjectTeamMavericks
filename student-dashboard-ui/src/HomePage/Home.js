import { useNavigate } from 'react-router-dom';

function Home() {
  return (
    <div
      style={{
        backgroundColor: "black",
        height: "100vh",
        width: "100vw",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        margin: 0,
        padding: 0,
        overflow: "hidden",
      }}
    >
      <h1 style={{ color: "white", textAlign: "center", margin: 0, padding: 0 }}>
        Welcome to Student Dashboard
      </h1>
    </div>
  );
}

export default Home;
