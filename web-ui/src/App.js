import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Container, Row, Col } from 'react-bootstrap';
import {Stack} from 'react-bootstrap-icons';

function App() {
  return (
    <div className="App">
      {/* Bootstrap Navbar */}
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home"><Stack/>MasterBI</Navbar.Brand>
        </Container>
      </Navbar>


      {/* Bootstrap Grid with Placeholders */}
      <Container className="my-4">
        <Row>
          <Col md={4}>
            <div className="placeholder" style={{ height: '200px', backgroundColor: '#e9ecef' }}>Placeholder 1</div>
          </Col>
          <Col md={4}>
            <div className="placeholder" style={{ height: '200px', backgroundColor: '#e9ecef' }}>Placeholder 2</div>
          </Col>
          <Col md={4}>
            <div className="placeholder" style={{ height: '200px', backgroundColor: '#e9ecef' }}>Placeholder 3</div>
          </Col>
        </Row>
      </Container>

      {/* Bootstrap Footer */}
      <footer className="bg-light text-center text-lg-start mt-auto">
        <div className="text-center p-3">
          MasterBI
        </div>
      </footer>
    </div>
  );
}

export default App;
