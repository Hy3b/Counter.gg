import { useState } from 'react'
import './App.css'

function App() {
  const [gameName, setGameName] = useState('')
  const [tagLine, setTagLine] = useState('')
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSearch = async () => {
    if (!gameName || !tagLine) return;
    setLoading(true);
    setResult(null);
    try {
      const response = await fetch(`http://localhost:8080/api/riot/summoner?gameName=${gameName}&tagLine=${tagLine}`);
      const data = await response.json();
      setResult(data);
    } catch (error) {
      setResult({ error: 'Không thể kết nối đến máy chủ Backend.' });
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="app-container">
      <h1 className="hero-title neon-text">Counter.gg</h1>
      <p className="hero-subtitle">Tra cứu thông tin tài khoản Riot (League of Legends)</p>
      
      <div className="search-box glass-panel">
        <div className="input-group">
          <input 
            type="text" 
            className="input-cyber"
            placeholder="Tên người chơi (VD: Levi)" 
            value={gameName}
            onChange={e => setGameName(e.target.value)}
          />
        </div>
        <div className="input-group">
          <input 
            type="text" 
            className="input-cyber"
            placeholder="Tag (VD: VN2)" 
            value={tagLine}
            onChange={e => setTagLine(e.target.value)}
          />
        </div>
        <button className="btn-cyber" onClick={handleSearch} disabled={loading}>
          {loading ? 'Đang quét...' : 'Tra cứu'}
        </button>
      </div>

      {result && (
        <div className="result-container glass-panel">
          {result.error ? (
            <p className="error-text">{result.error}</p>
          ) : (
            <div>
              <div className="profile-header">
                {result.profileIconId && (
                  <img 
                    className="profile-icon"
                    src={`https://ddragon.leagueoflegends.com/cdn/14.11.1/img/profileicon/${result.profileIconId}.png`} 
                    alt="Profile Icon" 
                  />
                )}
                <div className="profile-info">
                  <h3>{gameName} <span style={{color: 'var(--text-muted)'}}>#{tagLine}</span></h3>
                  {result.summonerLevel && (
                    <div className="level-badge">Cấp {result.summonerLevel}</div>
                  )}
                </div>
              </div>
              <h4 className="neon-text-blue">Raw Data</h4>
              <pre className="raw-data">
                <code>{JSON.stringify(result, null, 2)}</code>
              </pre>
            </div>
          )}
        </div>
      )}
    </div>
  )
}

export default App
