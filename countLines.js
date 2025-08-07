const fs = require('fs');
const path = require('path');

const filePath = path.join(__dirname, 'output', 'massaDeDados.csv');

try {
  const data = fs.readFileSync(filePath, 'utf8');
  const lines = data.trim().split('\n');
  console.log(`O arquivo contém ${lines.length} linhas.`);
} catch (err) {
  console.error('Erro ao ler o arquivo:', err);
}
