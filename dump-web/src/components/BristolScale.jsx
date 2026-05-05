import { BRISTOL_TYPES } from '../lib/bristol.js'

function BristolScale({ value, onChange }) {
  return (
    <div className="bristol-scale">
      <div className="bristol-grid">
        {BRISTOL_TYPES.map((type) => {
          const isSelected = value === type.value
          return (
            <button
              key={type.value}
              type="button"
              className={`bristol-card${isSelected ? ' selected' : ''}`}
              onClick={() => onChange(type.value)}
              style={{
                '--bristol-bg': type.bgColor,
                '--bristol-color': type.itemColor,
              }}
            >
              <div className="bristol-badge" style={{ background: type.itemColor }}>
                {type.value}
              </div>
              <div className="bristol-visual">
                <span
                  className="bristol-circle"
                  style={{
                    background: type.itemColor,
                    boxShadow: isSelected ? `0 0 0 3px ${type.itemColor}40` : 'none',
                  }}
                />
              </div>
              <div className="bristol-label">{type.label}</div>
            </button>
          )
        })}
      </div>
    </div>
  )
}

export default BristolScale
