package com.kingtest.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingtest.crypto.DataEncryptionService;
import com.kingtest.dto.requests.ConsultaInputDto;
import com.kingtest.dto.responses.ConsultaRepository;
import com.kingtest.entities.Consulta;
import com.kingtest.entities.Medico;
import com.kingtest.entities.Paciente;
import com.kingtest.exceptions.ValidationException;
import com.kingtest.exceptions.ValidationExceptionValues;

@Service
public class ConsultaService {
    private enum ReportQueryOptions {
        REPORTBY_DATE_ONLY, REPORTBY_DATE_DOCTORID, REPORTBY_DATE_PAYMENTMETHOD, REPORTBY_DATE_ALLFILTERS
    }

    @Autowired
    private DataEncryptionService dataEncryptionService;

    private ConsultaRepository consultaRepository;

    private boolean doctorIdIsNull;
    private boolean paymentMethodIsNull;
    private ReportQueryOptions selectedQueryOption;

    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public Consulta create(ConsultaInputDto consulta) {
        try {
            Consulta createdExame = consultaRepository.save(consulta.toEntity());

            return decryptConsultaData(createdExame);
        } catch (Exception e) {
            throw e;
        }
    }

    public void payConsultation(String idString) throws Exception {
        try {
            Long id = Long.valueOf(idString);
            Optional<Consulta> foundConsulta = consultaRepository.findById(id);

            if (foundConsulta.isPresent()) {
                Consulta consulta = foundConsulta.get();
                consulta.setPaid(true);

                consultaRepository.save(consulta);
            } else {
                throw new ValidationException(ValidationExceptionValues.PAT_FULLNAME_ALREADY_TAKEN);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public Consulta update(ConsultaInputDto consulta, String id) throws Exception {
        try {
            consulta.setId(Long.parseLong(id));

            Consulta createdExame = consultaRepository.save(consulta.toEntity());

            return decryptConsultaData(createdExame);
        } catch (Exception e) {
            throw e;
        }
    }

    public Consulta findById(long id) {
        Optional<Consulta> consulta = consultaRepository.findById(id);

        if (consulta.isPresent()) {
            return decryptConsultaData(consulta.get());
        } else {
            return null;
        }
    }

    public List<Consulta> findByMonthAndYear(String month, String year) {
        String searchString = year + "-" + month;
        List<Consulta> consulta = consultaRepository.findByDateStartsWith(searchString);

        return decryptEncryptDataOfConsultasList(consulta);
    }

    private List<Consulta> decryptEncryptDataOfConsultasList(List<Consulta> encryptedList) {
        return encryptedList
                .stream()
                .map(encryptedConsulta -> decryptConsultaData(encryptedConsulta))
                .collect(Collectors.toList());
    }

    private Consulta decryptConsultaData(Consulta consulta) {
        Medico encryptedMedico = consulta.getMedico();
        Medico decryptedMedico = dataEncryptionService.decryptMedico(encryptedMedico);
        consulta.setMedico(decryptedMedico);
        
        Paciente encryptedPaciente = consulta.getPaciente();
        Paciente decryptedPaciente = dataEncryptionService.decryptPaciente(encryptedPaciente);
        consulta.setPaciente(decryptedPaciente);

        return consulta;
    }

    public List<Consulta> findByDate(String date) {
        List<Consulta> consulta = consultaRepository.findByDateOrderByHour(date);
        return decryptEncryptDataOfConsultasList(consulta);
    }

    public List<Consulta> findReportByDateAndFilters(String date, Long medicoId, String paymentMethod) {
        doctorIdIsNull = medicoId == null;
        paymentMethodIsNull = paymentMethod == null;

        selectQueryToBeUsed();

        List<Consulta> consultas = useSelectedQuery(date, medicoId, paymentMethod);

        return decryptEncryptDataOfConsultasList(consultas);
    }

    private void selectQueryToBeUsed() {
        if (doctorIdIsNull && paymentMethodIsNull) {
            selectedQueryOption = ReportQueryOptions.REPORTBY_DATE_ONLY;
        } else if (doctorIdIsNull && !paymentMethodIsNull) {
            selectedQueryOption = ReportQueryOptions.REPORTBY_DATE_PAYMENTMETHOD;
        } else if (!doctorIdIsNull && paymentMethodIsNull) {
            selectedQueryOption = ReportQueryOptions.REPORTBY_DATE_DOCTORID;
        } else {
            selectedQueryOption = ReportQueryOptions.REPORTBY_DATE_ALLFILTERS;
        }
    }

    private List<Consulta> useSelectedQuery(String date, Long medicoId, String paymentMethod) {
        switch (selectedQueryOption) {
            case REPORTBY_DATE_ONLY:
                return consultaRepository.findByDateOrderByHour(date);

            case REPORTBY_DATE_DOCTORID:
                return consultaRepository.findByDateStartsWithAndMedicoIdOrderByHour(date, medicoId);

            case REPORTBY_DATE_PAYMENTMETHOD:
                return consultaRepository.findByDateStartsWithAndPaymentMethodOrderByHour(date, paymentMethod);

            default: // REPORTBY_DATE_ALLFILTERS
                return consultaRepository.findByDateStartsWithAndMedicoIdAndPaymentMethodOrderByHour(date, medicoId,
                        paymentMethod);
        }
    }
}
