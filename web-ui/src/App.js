import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Container } from 'react-bootstrap';
import { Stack } from 'react-bootstrap-icons';
import Dashboard from './components/Dashboard';

function App() {
  return (
    <div className="App">
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home"><Stack />MasterBI</Navbar.Brand>
        </Container>
      </Navbar>

      <Dashboard dashboardName="Sales" />

      <footer className="bg-light text-center text-lg-start mt-auto">
        <div className="text-center p-3">
          MasterBI
        </div>
      </footer>
    </div>
  );
}

export default App;