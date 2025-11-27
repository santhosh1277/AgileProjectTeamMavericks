import React from "react";

function Footer() {
  return (
    <footer
      style={{
        background: "linear-gradient(135deg, #667eea 0%, #764ba2 100%)",
        color: "#ffffff",
        padding: "60px 20px 20px",
        marginTop: "auto",
        borderTop: "3px solid rgba(255, 255, 255, 0.2)",
      }}
    >
      <div className="container text-center">
        <h4 style={{ 
          color: "#ffffff", 
          marginBottom: "20px", 
          fontWeight: "700",
          fontSize: "2rem",
          textShadow: "0 2px 10px rgba(0, 0, 0, 0.2)",
        }}>
          🌍 Where will your studies take you?
        </h4>

        <p style={{ 
          maxWidth: "800px", 
          margin: "0 auto 30px", 
          fontSize: "16px", 
          lineHeight: "1.8",
          opacity: "0.95",
          fontWeight: "400",
        }}>
          At UniMatch, we believe that students who study abroad become the next generation  
          of globally-minded adventurers and leaders.  
          Every year, our platform helps students discover top universities and schools around the world.
        </p>

        <div style={{ marginBottom: "30px" }}>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            About us
          </a>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            Promote your program
          </a>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            Student partnerships
          </a>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            Accessibility
          </a>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            Privacy policy
          </a>
          <a 
            href="#" 
            style={{ 
              color: "#ffffff", 
              margin: "0 12px",
              textDecoration: "none",
              fontWeight: "500",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "translateY(-2px)";
              e.target.style.textShadow = "0 0 10px rgba(255, 255, 255, 0.8)";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "translateY(0)";
              e.target.style.textShadow = "none";
            }}
          >
            Terms & conditions
          </a>
        </div>

        {/* Social icons */}
        <div style={{ 
          fontSize: "28px", 
          marginBottom: "20px",
        }}>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            📸
          </span>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            🎵
          </span>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            ▶️
          </span>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            💼
          </span>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            🎮
          </span>
          <span 
            style={{ 
              margin: "0 15px",
              cursor: "pointer",
              transition: "all 0.3s ease",
              display: "inline-block",
            }}
            onMouseOver={(e) => {
              e.target.style.transform = "scale(1.3)";
              e.target.style.filter = "drop-shadow(0 0 8px rgba(255, 255, 255, 0.8))";
            }}
            onMouseOut={(e) => {
              e.target.style.transform = "scale(1)";
              e.target.style.filter = "none";
            }}
          >
            🎙️
          </span>
        </div>

        <div 
          style={{ 
            borderTop: "2px solid rgba(255, 255, 255, 0.2)",
            paddingTop: "20px",
            marginTop: "20px",
            fontSize: "14px",
            opacity: "0.9",
            fontWeight: "500",
          }}
        >
          © 2024 UniMatch. All rights reserved.
        </div>
      </div>
    </footer>
  );
}

export default Footer;
