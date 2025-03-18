export const generateDummyData = (type) => {
    const data = [];
    for (let i = 0; i < 50; i++) {
      data.push({ x: i, y: Math.random() * 100 });
    }
    return data;
  };