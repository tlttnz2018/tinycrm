import React, {Component} from 'react';
import axios from 'axios';
import EditableNote from "./EditableNote";
import NoteCreatingForm from "./NoteCreatingForm";
import {Input} from 'reactstrap';

const customersUrl = "http://localhost:8080/api/customers";

class CustomerDetail extends Component {
    state = {
        notes: [],
        pageSize: 5,
        totalPages: 0,
        pageNumber: 0,
        customer: null,
        customerId: null,
        isAddNoteFormOpen: false,
    };
    loadNotesFromServer = (customerId) => {
        const {pageSize, pageNumber} = this.state;
        const self = this;

        axios.all([
            axios.get(`${customersUrl}/${customerId}`),
            axios.get(`${customersUrl}/${customerId}/notes?sort=id,desc&size=${pageSize}&page=${pageNumber}`)
        ]).then(axios.spread((customerDetailResult, customerNotesResult) => {
            const customer = customerDetailResult.data;
            const data = customerNotesResult.data;
            const {totalPages, number} = data;
            self.setState({customerId, customer, notes: data.content, totalPages, pageNumber: number});
        })).catch(error => {
            console.log(error.response)
        });
    };

    handlePrevious = () => this.handlePage(this.state.pageNumber - 1);
    handleNext = () => this.handlePage(this.state.pageNumber + 1);

    handlePage = (pageNo, force) => {
        const {pageNumber, totalPages, customerId} = this.state;

        if (!force && ((pageNo < 0) || (pageNo + 1 > totalPages) || (pageNo === pageNumber))) {
            return;
        }

        this.setState({pageNumber: pageNo}, () => this.loadNotesFromServer(customerId));
    };

    addNote = ({customerId, noteSubject, noteBody}) => {
        const self = this;
        this.toggleAddNoteForm();
        axios.post(`${customersUrl}/${customerId}/notes/`, {noteSubject, noteBody})
            .then(() => self.handlePage(0, true) // Reset to reflect new note added
            ).catch(error => {
                console.log(error.response)
        });
    };

    updateNote = ({customerId, id, noteSubject, noteBody}) => {
        const self = this;

        axios.put(`${customersUrl}/${customerId}/notes/${id}`, {noteSubject, noteBody})
            .then(() => self.loadNotesFromServer(customerId))
            .catch(error => {
                console.log(error.response)
        });
    };

    toggleAddNoteForm = () => this.setState({isAddNoteFormOpen: !this.state.isAddNoteFormOpen});

    componentDidMount = () => {
        const {customerId} = this.props.match.params;
        this.loadNotesFromServer(customerId);
    };

    changeStatus = ({ target: { value } }) => {
        const {customer, customerId} = this.state;
        const self = this;

        const updatedCustomer = {
            ...customer,
            status: value,
        };

        axios.put(`${customersUrl}/${customerId}`, updatedCustomer)
            .then(() => self.setState({customer: updatedCustomer}))
            .catch(error => {
                console.log(error.response)
        });
    };

    render() {
        const {notes, pageNumber, totalPages, customerId, customer, isAddNoteFormOpen} = this.state;
        const self = this;
        const rows = [];
        const pages = [];

        notes.forEach(function (note, index) {
            rows.push(<EditableNote key={index} index={index} customerId={customerId} note={note}
                                    onUpdateNote={self.updateNote}/>);
        });

        for (let idx = 1; idx <= totalPages; idx++) {
            pages.push(<li key={idx} className={(idx === (pageNumber + 1))? "page-item active" : "page-item"}>
                <button className="page-link" onClick={() => this.handlePage(idx - 1)}>{idx}</button>
            </li>);
        }

        return (
            <div className="container">
                <div className="panel panel-primary">
                    <div className="panel-heading">
                        <h3 className="d-flex justify-content-end panel-title border-bottom">Detail</h3>
                    </div>
                    {
                        !customer
                            ? null :
                            (
                                <address>
                                    <strong>{customer.name}</strong><br/>
                                    {customer.address} <br/>
                                    {customer.zipCode} <br/>
                                    <abbr title="Phone">P:</abbr> {customer.phone} <br/>
                                    <a href="mailto:#">{customer.email}</a>
                                    <hr/>

                                </address>
                        )
                    }
                </div>
                {
                    !customer
                        ? null:
                        (
                            <div className="panel panel-primary mt-5">
                                <div className="panel-heading">
                                    <h3 className="d-flex justify-content-end panel-title border-bottom">Status</h3>
                                </div>
                                <div className="mb-5">
                                    <Input type="select" className="w-25" value={customer.status} onChange={this.changeStatus}>
                                        <option value="CURRENT">Current</option>
                                        <option value="NON_ACTIVE">Non active</option>
                                        <option value="PROSPECT">Prospect</option>
                                    </Input>
                                </div>
                            </div>
                        )
                }
                <div className="panel panel-primary">
                    <div className="panel-heading">
                        <h3 className="d-flex justify-content-end panel-title border-bottom">Notes</h3>
                    </div>
                    <div id="accordion">
                        {rows}
                    </div>
                    {
                        !rows.length
                        ? null:
                            (<ul className="pagination justify-content-center flex-wrap">
                                <li className="page-item">
                                    <button className="page-link" onClick={this.handlePrevious}>Previous</button>
                                </li>
                                {pages}
                                <li className="page-item">
                                    <button className="page-link" onClick={this.handleNext}>Next</button>
                                </li>
                            </ul>)
                    }

                </div>
                <button type="button" className="btn btn-primary" onClick={this.toggleAddNoteForm}>Add note</button>
                <NoteCreatingForm customerId={customerId} isFormOpen={isAddNoteFormOpen} onSubmit={this.addNote}
                                  onCancel={this.toggleAddNoteForm}/>
            </div>
        );
    }
}

export default CustomerDetail;
