import React from "react";

function Footer() {
  return (
    <footer
      style={{
        backgroundColor: "#111111",
        color: "#e5e7eb",
        padding: "40px 20px",
        marginTop: "40px",
      }}
    >
      <div className="container text-center">
        <h4 style={{ color: "#ffffff", marginBottom: "15px", fontWeight: "600" }}>
          Where will your studies take you?
        </h4>

        <p style={{ maxWidth: "700px", margin: "0 auto 20px", fontSize: "15px", lineHeight: "1.6" }}>
          At UniMatch, we believe that students who study abroad become the next generation  
          of globally-minded adventurers and leaders.  
          Every year, our platform helps students discover top universities and schools around the world.
        </p>

        <div style={{ marginBottom: "20px" }}>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>About us</a>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>Promote your program</a>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>Student partnerships</a>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>Accessibility</a>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>Privacy policy</a>
          <a href="#" style={{ color: "#3b82f6", margin: "0 10px" }}>Terms & conditions</a>
        </div>

        {/* Social icons (dummy icons using emoji — can be replaced later) */}
        <div style={{ fontSize: "24px" }}>
          <span style={{ margin: "0 10px" }}>📸</span>
          <span style={{ margin: "0 10px" }}>🎵</span>
          <span style={{ margin: "0 10px" }}>▶️</span>
          <span style={{ margin: "0 10px" }}>💼</span>
          <span style={{ margin: "0 10px" }}>🎮</span>
          <span style={{ margin: "0 10px" }}>🎙️</span>
        </div>
      </div>
    </footer>
  );
}

export default Footer;
