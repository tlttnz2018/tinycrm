import React from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter as Router, Route } from 'react-router-dom';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap/dist/js/bootstrap.bundle.min';
import '../node_modules/font-awesome/css/font-awesome.min.css';
import './index.css';
import CustomerDetail from './components/CustomerDetail';
import Overview from './components/Overview';

ReactDOM.render(
    <div className="container">
        <div className="d-flex justify-content-end pb-2 mt-4 mb-5 text-primary">
            <h1>Tiny CRM</h1>
        </div>
        <Router>
            <div>
                <Route exact path='/' component={Overview} />
                <Route path='/detail/:customerId' component={CustomerDetail} />
            </div>
        </Router>
    </div>,
    document.getElementById('root')
);