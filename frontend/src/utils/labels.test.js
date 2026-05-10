import { describe, expect, it } from 'vitest'
import { labelOf, resultTone, statusLabels } from './labels'

describe('labels helpers', () => {
  it('returns readable status labels', () => {
    expect(labelOf(statusLabels, 'PENDING')).toBe('待审核')
    expect(labelOf(statusLabels, 'UNKNOWN')).toBe('UNKNOWN')
  })

  it('maps test result level to tag tone', () => {
    expect(resultTone('状态良好')).toBe('success')
    expect(resultTone('轻度压力')).toBe('warning')
    expect(resultTone('建议预约咨询')).toBe('danger')
  })
})
