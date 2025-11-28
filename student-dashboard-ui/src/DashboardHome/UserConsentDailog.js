import React, { useState } from "react";
import { Modal, Button } from "react-bootstrap";
import axios from "axios";

const ContactDialog = ({ show, onClose }) => {
  const rawUser = localStorage.getItem("user");
  const email = rawUser ? rawUser.replace(/"/g, "") : null;
  const [showSuccess, setShowSuccess] = useState(false);

  const handleYes = async () => {
    if (!email) {
      alert("Email not found in localStorage");
      return;
    }

    const payload = {
      email: email,
      consentGiven : true,
    };

    try {
      await axios.post("http://localhost:8080/api/students/consent", payload);
      setShowSuccess(true);
      setTimeout(() => {
        setShowSuccess(false);
        onClose();
      }, 2500);
    } catch (err) {
      console.error(err);
      alert("Failed to send request.");
    }
  };

  return (
    <>
      <Modal show={show} onHide={onClose} centered>
        <Modal.Header closeButton>
          <Modal.Title>Need Assistance?</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p>Do you want our executive to contact you? It's completely free.</p>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={onClose}>
            No
          </Button>
          <Button variant="primary" onClick={handleYes}>
            Yes
          </Button>
        </Modal.Footer>
      </Modal>

      {/* Success Animation Modal */}
      <Modal 
        show={showSuccess} 
        centered 
        backdrop="static"
        className="success-modal"
      >
        <Modal.Body className="text-center p-5">
          <div className="success-animation">
            <svg className="checkmark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 52 52">
              <circle className="checkmark-circle" cx="26" cy="26" r="25" fill="none"/>
              <path className="checkmark-check" fill="none" d="M14.1 27.2l7.1 7.2 16.7-16.8"/>
            </svg>
          </div>
          <h3 className="mt-4 text-success">Success!</h3>
          <p className="text-muted">Our executive will contact you soon!</p>
        </Modal.Body>
      </Modal>

      <style>{`
        .success-animation {
          margin: 0 auto;
          width: 100px;
          height: 100px;
        }

        .checkmark {
          width: 100px;
          height: 100px;
          border-radius: 50%;
          display: block;
          stroke-width: 3;
          stroke: #4CAF50;
          stroke-miterlimit: 10;
          box-shadow: inset 0px 0px 0px #4CAF50;
          animation: fill 0.4s ease-in-out 0.4s forwards, scale 0.3s ease-in-out 0.9s both;
        }

        .checkmark-circle {
          stroke-dasharray: 166;
          stroke-dashoffset: 166;
          stroke-width: 3;
          stroke-miterlimit: 10;
          stroke: #4CAF50;
          fill: none;
          animation: stroke 0.6s cubic-bezier(0.65, 0, 0.45, 1) forwards;
        }

        .checkmark-check {
          transform-origin: 50% 50%;
          stroke-dasharray: 48;
          stroke-dashoffset: 48;
          stroke: #4CAF50;
          animation: stroke 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.8s forwards;
        }

        @keyframes stroke {
          100% {
            stroke-dashoffset: 0;
          }
        }

        @keyframes scale {
          0%, 100% {
            transform: none;
          }
          50% {
            transform: scale3d(1.1, 1.1, 1);
          }
        }

        @keyframes fill {
          100% {
            box-shadow: inset 0px 0px 0px 30px #4CAF50;
          }
        }

        .success-modal .modal-content {
          border: none;
          border-radius: 15px;
          animation: slideInFromTop 0.4s ease-out;
        }

        @keyframes slideInFromTop {
          from {
            opacity: 0;
            transform: translateY(-50px);
          }
          to {
            opacity: 1;
            transform: translateY(0);
          }
        }
      `}</style>
    </>
  );
};

export default ContactDialog;
