export const BRISTOL_TYPES = [
  { value: 1, label: '硬块状', desc: '分离的硬块，像坚果', itemColor: '#9A5B15', bgColor: '#FEE7D3' },
  { value: 2, label: '香肠状带结块', desc: '香肠形但表面凹凸', itemColor: '#8B5E34', bgColor: '#F4DEC8' },
  { value: 3, label: '香肠状有裂缝', desc: '表面有裂痕', itemColor: '#8D5E13', bgColor: '#F6E1BF' },
  { value: 4, label: '光滑香肠状', desc: '理想形态，表面光滑柔软', itemColor: '#8A685E', bgColor: '#F1D9D0' },
  { value: 5, label: '柔软小块', desc: '边缘清晰的柔软团块', itemColor: '#8A7006', bgColor: '#FAEDB9' },
  { value: 6, label: '糊状', desc: '边缘参差的蓬松碎片', itemColor: '#1A5FB4', bgColor: '#DDEBFF' },
  { value: 7, label: '水样', desc: '完全液态，无固体', itemColor: '#136B73', bgColor: '#D8F4F6' },
]

export const SYMPTOM_TAGS = [
  { key: 'Straining', label: '费力' },
  { key: 'Pain-free', label: '无痛' },
  { key: 'Bloating', label: '腹胀' },
  { key: 'Blood', label: '便血' },
  { key: 'Urgency', label: '急迫' },
  { key: 'Constipation', label: '便秘' },
  { key: 'Diarrhea', label: '腹泻' },
  { key: 'Mucus', label: '粘液' },
  { key: 'Incomplete', label: '不尽感' },
]

export function getBristolType(value) {
  return BRISTOL_TYPES.find((t) => t.value === value) || null
}
