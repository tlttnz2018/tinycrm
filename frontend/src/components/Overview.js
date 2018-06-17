import React, {Component} from 'react';
import CustomerTable from './CustomerTable';
import axios from "axios/index";
import {CUSTOMERS_URL, SORT_DESC, SORT_ASC} from "./Constants";

class Overview extends Component {
    state = {
        customers: [],
        pageSize: 5,
        totalPages: 0,
        pageNumber: 0,
        sortField: '',
        sortOrder: '',
        field: '',
        query: '',
        filter: '',
    };

    filterStatus = (status) => {
        const {filter} = this.state;
        let {pageNumber} = this.state;
        let newFilter = '';
        if(status && status !== "ALL") {
            newFilter = status;
        }

        if (filter !== newFilter) {
            pageNumber = 0;
        }

        this.setState({filter: newFilter, pageNumber}, () => {
                this.loadCustomersFromServer();
            }
        );
    };

    handleSearch = (conditions) => {
        const {field, query} = this.state;
        let newField = '';
        let newQuery = '';
        let {pageNumber} = this.state;

        if(!conditions) {
            return;
        }
        if(!!conditions.query) {
            newField = conditions.field;
            newQuery = conditions.query;
        }

        if ((field !== newField) || (query !== newQuery)) {
            pageNumber = 0;
        }

        this.setState({field: newField, query: newQuery , pageNumber},
            () => {
                this.loadCustomersFromServer();
            }
        );
    };

    handleSort = (fieldName) => {
        let {sortField, sortOrder} = this.state;
        if (sortField === fieldName) {
            //toggle order
            sortOrder = this.toggleSortOrder(sortOrder);
        } else {
            // new then default is desc
            sortField = fieldName;
            sortOrder = SORT_DESC;
        }
        this.setState({sortField, sortOrder},
            () => {
                this.loadCustomersFromServer();
            });
    };

    loadCustomersFromServer = () => {
        const self = this;
        // const {pageSize, pageNumber} = this.state;
        const urlRequest = this.buildCustomersUrl();
        axios.get(urlRequest)
            .then(result => {
                const data = result.data;
                const {totalPages, number} = data;
                self.setState({customers: data.content, totalPages, pageNumber: number});
            }).catch(error => {
            console.log(error.response)
        });
    };

    buildCustomersUrl = () => {
        const {pageNumber, pageSize, sortField, sortOrder, field, query, filter} = this.state;
        const params = [];
        params.push("size=" + pageSize);
        params.push("page=" + pageNumber);

        if (!!sortField && !!sortOrder) {
            params.push("sort=" + sortField + "," + sortOrder);
        }

        if (!!field && !!query) {
            params.push("field=" + field);
            params.push("query=" + query);
        }

        if(!!filter) {
            params.push("filter=" + filter);
        }

        return `${CUSTOMERS_URL}?${params.join('&')}`;
    };

    toggleSortOrder = (sortOrder) => {
        return sortOrder === SORT_ASC ? SORT_DESC : SORT_ASC;
    };

    handlePrevious = () => {
        const {pageNumber} = this.state;
        if (pageNumber <= 0) {
            return;
        }
        this.setState({pageNumber: pageNumber - 1}, () => {
            this.loadCustomersFromServer();
        });
    };

    handleNext = () => {
        const {pageNumber, totalPages} = this.state;
        if (pageNumber + 1 >= totalPages) {
            return;
        }
        this.setState({pageNumber: pageNumber + 1}, () => {
            this.loadCustomersFromServer();
        });
    };

    handlePage = (pageNo, force) => {
        const {pageNumber, totalPages} = this.state;

        function isSamePage() {
            return (pageNo === pageNumber);
        }

        function isOutOfRange() {
            return (pageNo < 0) || (pageNo + 1 > totalPages);
        }

        if (!force && (isOutOfRange() || isSamePage())) {
            return;
        }

        this.setState({pageNumber: pageNo}, () => {
            this.loadCustomersFromServer();
        });
    };

    componentDidMount () {
        this.loadCustomersFromServer();
    };

    render() {
        const {customers, pageNumber, totalPages,sortField, sortOrder} = this.state;
        const pages = [];
        for (let idx = 1; idx <= totalPages; idx++) {
            pages.push(<li key={idx} className={(idx === (pageNumber + 1))? "page-item active" : "page-item"}>
                <button className="page-link" onClick={() => this.handlePage(idx - 1)}>{idx}</button>
            </li>);
        }
        return (
            <div className="container">
                <CustomerTable customers={customers} onFilterStatus={this.filterStatus} onSearch={this.handleSearch} onSort={this.handleSort} sortField={sortField} sortOrder={sortOrder}/>
                {
                    !customers.length
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
        );
    }
}

export default Overview;
