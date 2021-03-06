import React, { FC, useState } from 'react';
import { SubmitHandler, useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { loginFormSchema } from '../../../../application/formSchemas/RegisterAndLoginPageSchemas';
import { useUser } from '../../../../application/hooks/useUser';
import { Button, TextField } from '@mui/material';
import cookies from 'js-cookie';
import {
  AlertStyled,
  LoginAndRegisterFormStyled
} from './RegisterAndLoginRoutes.style';
import { useHistory } from 'react-router-dom';

export interface LoginInputs {
  email: string;
  password: string;
}

const LoginPageForm: FC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm({ resolver: yupResolver(loginFormSchema) });
  const [error, setError] = useState(false);
  const { useLogin, getCurrentUserAndReload } = useUser();
  const { mutate } = useLogin();
  const { replace } = useHistory();

  const onSubmit: SubmitHandler<LoginInputs> = (data) => {
    mutate(data, {
      onSuccess: async (data) => {
        cookies.set('token', data.data.token);
        await getCurrentUserAndReload();
        replace('/');
      },
      onError: () => {
        setError(true);
      }
    });
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} noValidate>
      <h2>Zaloguj się</h2>
      <TextField
        margin="normal"
        label="E-mail"
        size="small"
        type="email"
        fullWidth={true}
        variant="outlined"
        error={!!errors.email}
        helperText={errors.email?.message}
        {...register('email')}
      />
      <TextField
        margin="normal"
        label="Hasło"
        size="small"
        type="password"
        fullWidth={true}
        variant="outlined"
        error={!!errors.password}
        helperText={errors.password?.message}
        {...register('password')}
      />
      <Button
        fullWidth={true}
        variant="contained"
        color="primary"
        type="submit"
      >
        Zaloguj
      </Button>
      {error && (
        <AlertStyled severity="error">Niepoprawny email lub hasło</AlertStyled>
      )}
    </form>
  );
};

export const LoginPage: FC = () => (
  <LoginAndRegisterFormStyled>
    <LoginPageForm />
  </LoginAndRegisterFormStyled>
);
