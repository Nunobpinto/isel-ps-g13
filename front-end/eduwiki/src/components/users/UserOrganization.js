import React from 'react'
import {Card} from 'antd'

export default (props) => (
  <div className='side_item'>
    <Card
      title='My organization'
      actions={[
        <p onClick={() => props.history.push('/organization')}>
                    See full organization page
        </p>
      ]}
    >
      <h1>ISEL</h1>
    </Card>
  </div>
)
