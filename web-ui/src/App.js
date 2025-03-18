import 'bootstrap/dist/css/bootstrap.min.css';
import { Navbar, Container} from 'react-bootstrap';
import {Stack} from 'react-bootstrap-icons';
import Dashboard from './components/Dashboard';

function App() {
  let config = {
    "name": "Foo",
    "grid": [
      [
        {
          "type": "HISTOGRAM",
          "name": "foo",
          "abscissa": {
            "name": "x",
            "scaleType": "DEFAULT"
          },
          "ordinate": {
            "name": "y",
            "scaleType": "DEFAULT"
          }
        },
        {
          "type": "HISTOGRAM",
          "name": "foo",
          "abscissa": {
            "name": "x",
            "scaleType": "DEFAULT"
          },
          "ordinate": {
            "name": "y",
            "scaleType": "DEFAULT"
          }
        },
        {
          "type": "LINE_CHART",
          "name": "bar",
          "abscissa": {
            "name": "x",
            "scaleType": "DEFAULT"
          },
          "ordinate": {
            "name": "y",
            "scaleType": "DEFAULT"
          }
        }
      ]
    ]
  }
  return (
    <div className="App">
      {/* Bootstrap Navbar */}
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home"><Stack/>MasterBI</Navbar.Brand>
        </Container>
      </Navbar>


      <Dashboard config={config}/>

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
