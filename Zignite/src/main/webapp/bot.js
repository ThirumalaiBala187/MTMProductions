const container = document.querySelector('.bot-container');
        const particles = document.querySelector('.particles');
        const messages = [
            "Let's learn something new! 📚",
            "I'm here to help! 💡",
            "Ready for a challenge? 🎯",
            "Knowledge is power! ⚡",
            "You're doing great! 🌟"
        ];

        function createParticle(x, y) {
            const particle = document.createElement('div');
            particle.className = 'particle';
            
            const size = Math.random() * 8 + 4;
            particle.style.width = `${size}px`;
            particle.style.height = `${size}px`;
            
            particle.style.left = `${x}px`;
            particle.style.top = `${y}px`;
            
            const tx = (Math.random() - 0.5) * 100;
            const ty = (Math.random() - 0.5) * 100;
            particle.style.setProperty('--tx', `${tx}px`);
            particle.style.setProperty('--ty', `${ty}px`);
            
            particle.style.animation = 'particle-float 1s forwards';
            
            particles.appendChild(particle);
            setTimeout(() => particle.remove(), 1000);
        }

        function emitParticles(e) {
            const rect = container.getBoundingClientRect();
            const x = e.clientX - rect.left;
            const y = e.clientY - rect.top;
            
            for (let i = 0; i < 8; i++) {
                createParticle(x, y);
            }
        }

        let messageIndex = 0;
        function changeMessage() {
            const messageElement = document.querySelector('.message');
            messageElement.style.opacity = 0;
            
            setTimeout(() => {
                messageElement.textContent = messages[messageIndex];
                messageElement.style.opacity = 1;
                messageIndex = (messageIndex + 1) % messages.length;
            }, 300);
        }

        container.addEventListener('mousemove', emitParticles);
        container.addEventListener('click', () => {
            changeMessage();
            emitParticles(event);
            container.style.transform = 'scale(0.95)';
            setTimeout(() => container.style.transform = 'scale(1)', 150);
        });

