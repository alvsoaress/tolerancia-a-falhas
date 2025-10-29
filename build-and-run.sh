
echo "Building all microservices..."

echo ""
echo "Building IMD Travel..."
cd imd-travel
mvn clean package -DskipTests
cd ..

echo ""
echo "Building Airlines Hub..."
cd airlines-hub
mvn clean package -DskipTests
cd ..

echo ""
echo "Building Exchange..."
cd exchange
mvn clean package -DskipTests
cd ..

echo ""
echo "Building Fidelity..."
cd fidelity
mvn clean package -DskipTests
cd ..

echo ""
echo "All microservices built successfully!"
echo ""
echo "Starting Docker Compose..."
docker-compose up --build
