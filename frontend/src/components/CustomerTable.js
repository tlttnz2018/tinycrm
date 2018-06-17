import React, {Component} from 'react';
import PropTypes from 'prop-types';
import {Button, Col, Form, FormGroup, Input, Label} from 'reactstrap';
import Customer from './Customer';

class CustomerTable extends Component {
    static propTypes = {
        sortField: PropTypes.string.isRequired,
        sortOrder: PropTypes.string
    };

    filterStatus = (event) => this.props.onFilterStatus(event.target.value);

    handleSearch = (event) => {
        const { target: {
            field,
            query
        } } = event;
        event.preventDefault();

        this.props.onSearch({
            field: field.value,
            query: query.value,
        });
    };

    handleSortCreatedDate = () => this.props.onSort("createdAt");

    handleSortName = () => this.props.onSort("name");

    handleSortStatus = () => this.props.onSort("status");

    render() {
        const {sortField, sortOrder} = this.props;

        const rows = [];
        this.props.customers.forEach(function(customer, index) {
            rows.push(<Customer key={index} customer={customer}/>);
        });

        return (
            <div className="panel panel-primary">
                <div className="panel-heading">
                    <h3 className="panel-title">Customers</h3>
                </div>
                <Form onSubmit={this.handleSearch} className="mb-2 mt-2">
                    <FormGroup row>
                        <Label sm={1} >Search</Label>
                        <Col sm={2}>
                            <Input type="select" name="field" id="fieldSearch">
                                <option value="name" default>Name</option>
                                <option value="address">Address</option>
                                <option value="email">Email</option>
                                <option value="phone">Phone</option>
                            </Input>
                        </Col>
                        <Col sm={6}>
                            <Input type="search" name="query" id="searchCustomer" placeholder="query text"/>
                        </Col>
                        <Button>Search</Button>
                    </FormGroup>
                </Form>
                <Form>
                    <FormGroup row>
                        <Label sm={1}> Status</Label>
                        <Col sm={2}>
                        <Input type="select" onChange={this.filterStatus} id="filterStatus">
                            <option value="ALL" default>All</option>
                            <option value="CURRENT">Current</option>
                            <option value="NON_ACTIVE">Non active</option>
                            <option value="PROSPECT">Prospect</option>
                        </Input>
                        </Col>
                    </FormGroup>
                </Form>
                <table className="table table-striped table-hover table-bordered">
                    <thead className="thead-light">
                    <tr>
                        <th onClick={this.handleSortName}>Name <i className={"fa fa-fw " + (sortField !== "name"? "fa-sort": "fa-sort-" + sortOrder)}/></th>
                        <th onClick={this.handleSortStatus}>Status <i className={"fa fa-fw " + (sortField !== "status"? "fa-sort": "fa-sort-" + sortOrder)}/></th>
                        <th onClick={this.handleSortCreatedDate}>Date created <i className={"fa fa-fw " + (sortField !== "createdAt"? "fa-sort": "fa-sort-" + sortOrder)}/></th><th/>
                    </tr>
                    </thead>
                    <tbody>
                    {rows}
                    </tbody>
                </table>
            </div>
            );
    }
}

export default CustomerTable;
