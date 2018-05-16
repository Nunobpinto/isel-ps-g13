import React from 'react'
import { Spin } from 'antd'

export default ({result, onJson, onLoading, onError}) => {
  if (result.loading) {
    if (onLoading) {
      return onLoading(result)
    } else {
      return <div> <Spin tip='Loading...' /> </div>
    }
  } else if (result.error) {
    if (onError) {
      return onError(result)
    } else {
      return <div> ERROR: {result.error.message}</div>
    }
  } else if (result.json) {
    return onJson(result.json)
  } else {
    return <div> Oops, invalid state </div>
  }
}
