import React, { useState } from 'react';
import logo from './logo.svg';
import './App.css';

function App() {
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileChange = (event) => {
    setSelectedFile(event.target.files[0]);
  };

  const handleSubmit = async () => {
    if (!selectedFile) {
      alert('Please select a PDF file.');
      return;
    }

    const formData = new FormData();
    formData.append('file', selectedFile);

    try {
      const response = await fetch('https://<your-function-app-name>.azurewebsites.net/api/HttpExample', {
        method: 'POST',
        body: formData,
      });

      if (response.ok) {
        const result = await response.json();
        console.log('Parsed text:', result.parsedText);
        // Handle the parsed text as needed (display it, process it, etc.)
      } else {
        console.error('Error:', response.status, response.statusText);
        // Handle error appropriately
      }
    } catch (error) {
      console.error('Error:', error.message);
      // Handle error appropriately
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>Your Azure website frontend is here!</p>
        <div>
          <input type="file" accept=".pdf" onChange={handleFileChange} />
          <button onClick={handleSubmit}>Submit</button>
        </div>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default App;
