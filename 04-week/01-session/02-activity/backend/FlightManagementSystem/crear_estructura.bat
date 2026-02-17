@echo off
REM Infraestructura
mkdir Infrastructure\Controller
mkdir Infrastructure\Entity
mkdir Infrastructure\IDTO
mkdir Infrastructure\IRepository
mkdir Infrastructure\IService
mkdir Infrastructure\Service
mkdir Infrastructure\Utils

type nul > Infrastructure\Controller\AirportController.java
type nul > Infrastructure\Controller\TerminalController.java
type nul > Infrastructure\Controller\BoardingGateController.java

type nul > Infrastructure\Entity\Airport.java
type nul > Infrastructure\Entity\Terminal.java
type nul > Infrastructure\Entity\BoardingGate.java

type nul > Infrastructure\IDTO\AirportDTO.java
type nul > Infrastructure\IDTO\TerminalDTO.java
type nul > Infrastructure\IDTO\BoardingGateDTO.java

type nul > Infrastructure\IRepository\IAirportRepository.java
type nul > Infrastructure\IRepository\ITerminalRepository.java
type nul > Infrastructure\IRepository\IBoardingGateRepository.java

type nul > Infrastructure\IService\IAirportService.java
type nul > Infrastructure\IService\ITerminalService.java
type nul > Infrastructure\IService\IBoardingGateService.java

type nul > Infrastructure\Service\AirportService.java
type nul > Infrastructure\Service\TerminalService.java
type nul > Infrastructure\Service\BoardingGateService.java

type nul > Infrastructure\Utils\ABaseUtils.java

REM Recursos Humanos
mkdir HumanResources\Controller
mkdir HumanResources\Entity
mkdir HumanResources\IDTO
mkdir HumanResources\IRepository
mkdir HumanResources\IService
mkdir HumanResources\Service
mkdir HumanResources\Utils

type nul > HumanResources\Controller\EmployeeController.java
type nul > HumanResources\Entity\Employee.java
type nul > HumanResources\IDTO\EmployeeDTO.java
type nul > HumanResources\IRepository\IEmployeeRepository.java
type nul > HumanResources\IService\IEmployeeService.java
type nul > HumanResources\Service\EmployeeService.java
type nul > HumanResources\Utils\ABaseUtils.java

echo Estructura creada correctamente.
pause