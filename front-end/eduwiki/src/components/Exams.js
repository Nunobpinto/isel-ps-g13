import React from 'react'
import {Card, Col, Row, Upload, Button, Icon} from 'antd'

export default class extends React.Component {
  render () {
    return (
      <div id={`exms_term_${this.props.termId}`}>
        {this.props.exams.map(exam => (
          <div style={{ padding: '30px' }}>
            <Row gutter={16}>
              <Col span={8} key={exam.id}>
                <Card title={`${exam.type} - ${exam.phase} - ${exam.dueDate}`}>
                  {exam.sheet}
                </Card>
              </Col>
            </Row>
          </div>
        ))}
        <Upload>
          <Button>
            <Icon type='upload' /> Click to Upload An Exam
          </Button>
        </Upload>
      </div>
    )
  }
}
