import React from 'react'
import fetcher from '../../fetcher'
import { Link } from 'react-router-dom'
import IconText from '../comms/IconText'
import Layout from '../layout/Layout'
import { Button, Input, message, List, Card } from 'antd'
import CreateClass from './CreateClass'
import timesetampParser from '../../timestampParser'
import config from '../../config'

export default (props) => (
  <Layout>
    <Classes />
  </Layout>
)

class Classes extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      classes: [{
        term: undefined,
        classes: []
      }],
      viewClasses: [{
        term: undefined,
        classes: []
      }],
      error: undefined,
      voteUp: false,
      voteDown: false,
      staged: [],
      viewStaged: [],
      nameFilter: '',
      stagedNameFilter: ''
    }
    this.voteUpStaged = this.voteUpStaged.bind(this)
    this.voteDownStaged = this.voteDownStaged.bind(this)
    this.filterStagedByName = this.filterStagedByName.bind(this)
    this.reorderClasses = this.reorderClasses.bind(this)
    this.createClass = this.createClass.bind(this)
    this.createDefinitiveClass = this.createDefinitiveClass.bind(this)
    this.createStagedClass = this.createStagedClass.bind(this)
    this.fetchStagedClasses = this.fetchStagedClasses.bind(this)
  }

  fetchStagedClasses () {
    const stagedUrl = config.API_PATH + '/classes/stage'
    const options = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(stagedUrl, options)
      .then(json => {
        const staged = json.classStageList
        this.setState({
          staged: staged,
          viewStaged: staged,
          stagedCourseView: false,
          seeStaged: true
        })
      })
      .catch(_ => {
        message.error('Error fetching staged classes')
        this.setState({stagedCourseView: false})
      })
  }

  filterStagedByName (ev) {
    const name = ev.target.value.toLowerCase()
    this.setState(prevState => {
      let array = prevState.staged
      array = array.filter(staged => staged.className.toLowerCase().includes(name))
      return ({viewStaged: array})
    })
  }

  render () {
    return (
      <div className='container'>
        <div className='left-div'>
          {this.state.error
            ? <p> Error getting all the classes please try again !!! </p>
            : <div>
              <h1>All classes in ISEL</h1>
              <List
                itemLayout='vertical'
                size='large'
                bordered
                dataSource={this.state.viewClasses}
                footer={<div><Button icon='plus' id='create_btn' type='primary' onClick={() => this.setState({stagedCourseView: true})}>See staged and Create Class</Button></div>}
                renderItem={item => (
                  <List.Item>
                    <Card title={item.term}>
                      <List
                        itemLayout='vertical'
                        bordered
                        dataSource={item.classes}
                        renderItem={klass => (
                          <List.Item>
                            <List.Item.Meta
                              title={<Link to={{ pathname: `/classes/${klass.classId}` }}>{klass.className}</Link>}
                              description={`Created by ${klass.createdBy} on ${klass.timestamp}`}
                            />
                          </List.Item>
                        )}
                      />
                    </Card>
                  </List.Item>
                )}
              />
            </div>
          }
        </div>
        <div class='right-div'>
          {
            this.state.seeStaged &&
              <div id='stagedCourses'>
                <h1>All staged Classes</h1>
                <p> Filter By Name : </p>
                <Input.Search
                  name='stagedNameFilter'
                  placeholder='Search name'
                  onChange={this.filterStagedByName}

                />
                <List id='staged-list'
                  grid={{ gutter: 50, column: 2 }}
                  dataSource={this.state.viewStaged}
                  renderItem={item => (
                    <List.Item>
                      <Card title={item.className}>
                        <p>Term : {item.lecturedTerm}</p>
                        <p>Programme : {item.programmeShortName}</p>
                        <p>Created By : <a href={`/users/${item.createdBy}`}>{item.createdBy}</a></p>
                        <p>Created at : {timesetampParser(item.timestamp)}</p>
                      </Card>
                      <IconText
                        type='like-o'
                        id='like_btn'
                        onClick={() =>
                          this.setState({
                            voteUpStaged: true,
                            stageID: item.stagedId
                          })}
                        text={item.votes}
                      />
                      <IconText
                        type='dislike-o'
                        id='dislike_btn'
                        onClick={() =>
                          this.setState({
                            voteDownStaged: true,
                            stageID: item.stagedId
                          })}
                      />
                    </List.Item>
                  )}
                />
                <Button type='primary' onClick={() => this.setState({createClassForm: true})}>Still want to create?</Button>
                {this.state.createClassForm &&
                  <CreateClass
                    auth={window.localStorage.getItem('auth')}
                    action={(data) => this.setState({data: data, createClassFlag: true})}
                  />}
              </div>
          }
        </div>
      </div>
    )
  }

  reorderClasses (data) {
    let newArray = []
    data.forEach(value => {
      const index = newArray.findIndex(element => element.term === value.lecturedTerm)
      if (index === -1) {
        let obj = {
          term: value.lecturedTerm,
          classes: [{
            classId: value.classId,
            createdBy: value.createdBy,
            className: value.className,
            termId: value.termId,
            votes: value.votes,
            timestamp: value.timestamp
          }]
        }
        newArray.push(obj)
      } else {
        newArray[index].classes.push({
          classId: value.classId,
          createdBy: value.createdBy,
          className: value.className,
          termId: value.termId,
          votes: value.votes,
          timestamp: value.timestamp
        })
      }
    })
    return newArray
  }

  componentDidMount () {
    const uri = config.API_PATH + '/classes/'
    const header = {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      }
    }
    fetcher(uri, header)
      .then(classes => {
        let orderedClasses = this.reorderClasses(classes.classList)
        this.setState({
          classes: orderedClasses,
          viewClasses: orderedClasses
        })
      })
      .catch(error => {
        message.error('Error fetching classes')
        this.setState({error: error})
      })
  }

  voteUpStaged () {
    const voteInput = {
      vote: 'Up'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/classes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => {
        message.success('Voted staged up!!!')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(klass => klass.stagedId === stageID)
          newArray[index].votes = prevState.staged[index].votes + 1
          return ({
            staged: newArray,
            voteUpStaged: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteUpStaged: false})
      })
  }

  voteDownStaged () {
    const voteInput = {
      vote: 'Down'
    }
    const stageID = this.state.stageID
    const url = `${config.API_PATH}/classes/stage/${stageID}/vote`
    const body = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Content-Type': 'application/json',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1'
      },
      body: JSON.stringify(voteInput)
    }
    fetcher(url, body)
      .then(_ => {
        message.success('Voted staged up!!!')
        this.setState(prevState => {
          let newArray = [...prevState.staged]
          const index = newArray.findIndex(klass => klass.stagedId === stageID)
          newArray[index].votes = prevState.staged[index].votes - 1
          return ({
            staged: newArray,
            voteDownStaged: false
          })
        })
      })
      .catch(error => {
        if (error.detail) {
          message.error(error.detail)
        } else {
          message.error('Cannot vote up')
        }
        this.setState({voteDownStaged: false})
      })
  }

  createDefinitiveClass () {
    const stagedUrl = config.API_PATH + '/classes'
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.state.data)
    }
    fetcher(stagedUrl, options)
      .then(klass => {
        message.success('Created Class')
        this.setState(prevState => {
          let array = prevState.classes
          const idx = array.findIndex(obj => obj.term === klass.lecturedTerm)
          array[idx].classes.push(klass)
          return ({
            classes: array,
            createClassFlag: false
          })
        })
      })
      .catch(_ => {
        message.error('Error creating Class')
        this.setState({
          createClassFlag: false
        })
      })
  }

  createStagedClass () {
    const stagedUrl = config.API_PATH + '/classes/stage'
    const options = {
      method: 'POST',
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Basic ' + window.localStorage.getItem('auth'),
        'tenant-uuid': '4cd93a0f-5b5c-4902-ae0a-181c780fedb1',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(this.state.data)
    }
    fetcher(stagedUrl, options)
      .then(staged => {
        message.success('Created Staged Class')
        this.setState(prevState => {
          prevState.staged.push(staged)
          return ({
            staged: prevState.staged,
            createClassFlag: false
          })
        })
      })
      .catch(_ => {
        message.error('Error creating Staged Class')
        this.setState({
          createClassFlag: false
        })
      })
  }

  createClass () {
    if (this.props.user.reputation.role === 'ROLE_ADMIN') {
      this.createDefinitiveClass()
    } else {
      this.createStagedClass()
    }
  }

  componentDidUpdate () {
    if (this.state.voteUpStaged) {
      this.voteUpStaged()
    } else if (this.state.voteDownStaged) {
      this.voteDownStaged()
    } else if (this.state.stagedCourseView) {
      this.fetchStagedClasses()
    } else if (this.state.createClassFlag) {
      this.createClass()
    }
  }
}
