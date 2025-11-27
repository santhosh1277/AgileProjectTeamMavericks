import React from "react";
import { Modal, Button } from "react-bootstrap";
import axios from "axios";

const ContactDialog = ({ show, onClose }) => {
  const rawUser = localStorage.getItem("user");
  const email = rawUser ? rawUser.replace(/"/g, "") : null;

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
      alert("Our executive will contact you soon!");
      onClose(); 
    } catch (err) {
      console.error(err);
      alert("Failed to send request.");
    }
  };

  return (
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
  );
};

export default ContactDialog;
