import { useState, type ReactNode } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { z } from 'zod'
import { zodResolver } from '@hookform/resolvers/zod'
import { useForm } from 'react-hook-form'
import { ArrowRight, Wallet } from 'lucide-react'
import { useAuth } from '../auth/AuthContext'
import { errorMessage } from '../api/client'

const schema = z.object({ email: z.email('Informe um e-mail válido.'), password: z.string().min(8, 'Use pelo menos 8 caracteres.') })
type Credentials = z.infer<typeof schema>
const registerSchema = schema.extend({ name: z.string().trim().min(2, 'Informe seu nome.').max(100) })
type RegisterFields = z.infer<typeof registerSchema>

function AuthFrame({ children, title, subtitle }: { children: ReactNode; title: string; subtitle: string }) {
  return <main className="auth-page"><section className="auth-brand-panel"><Link to="/login" className="brand brand-light"><span className="brand-mark"><Wallet size={20} /></span><span>saldo<span className="brand-dot">.</span></span></Link><div className="auth-quote"><div className="auth-orbit"><div className="orbit-dot" /><Wallet size={34} /></div><h2>Clareza para<br />decidir melhor.</h2><p>Organize o presente. Planeje o que vem pela frente.</p><div className="auth-brand-footer">Seu dinheiro, com mais intenção.</div></div></section><section className="auth-form-panel"><div className="auth-mobile-brand"><Link to="/login" className="brand"><span className="brand-mark"><Wallet size={20} /></span><span>saldo<span className="brand-dot">.</span></span></Link></div><div className="auth-form-wrap"><span className="eyebrow">GESTÃO FINANCEIRA PESSOAL</span><h1>{title}</h1><p className="muted auth-subtitle">{subtitle}</p>{children}<p className="auth-privacy">Seus dados financeiros são privados e protegidos pela sua conta.</p></div><span className="auth-copyright">© {new Date().getFullYear()} Saldo · Feito para simplificar.</span></section></main>
}

export function LoginPage() {
  const { signIn } = useAuth()
  const navigate = useNavigate()
  const [message, setMessage] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { register, handleSubmit, formState: { errors } } = useForm<Credentials>({ resolver: zodResolver(schema) })
  const submit = async (data: Credentials) => {
    setMessage(''); setSubmitting(true)
    try { await signIn(data.email, data.password); navigate('/', { replace: true }) }
    catch (error) { setMessage(errorMessage(error, 'E-mail ou senha inválidos.')) }
    finally { setSubmitting(false) }
  }
  return <AuthFrame title="Boas-vindas de volta" subtitle="Entre para acompanhar sua vida financeira.">
    <form className="form-stack" onSubmit={handleSubmit(submit)} noValidate>
      <label className="field"><span>E-mail</span><input type="email" autoComplete="email" placeholder="voce@email.com" {...register('email')} />{errors.email && <small className="field-error">{errors.email.message}</small>}</label>
      <label className="field"><span>Senha</span><input type="password" autoComplete="current-password" placeholder="Sua senha" {...register('password')} />{errors.password && <small className="field-error">{errors.password.message}</small>}</label>
      {message && <div className="alert alert-error" role="alert">{message}</div>}
      <button className="button button-primary button-wide" disabled={submitting}>{submitting ? 'Entrando…' : 'Entrar'}<ArrowRight size={17} /></button>
    </form>
    <p className="auth-switch">Ainda não tem uma conta? <Link to="/register">Crie sua conta</Link></p>
  </AuthFrame>
}

export function RegisterPage() {
  const { signUp } = useAuth()
  const navigate = useNavigate()
  const [message, setMessage] = useState('')
  const [submitting, setSubmitting] = useState(false)
  const { register, handleSubmit, formState: { errors } } = useForm<RegisterFields>({ resolver: zodResolver(registerSchema) })
  const submit = async (data: RegisterFields) => {
    setMessage(''); setSubmitting(true)
    try { await signUp(data.name, data.email, data.password); navigate('/', { replace: true }) }
    catch (error) { setMessage(errorMessage(error, 'Não foi possível criar a conta.')) }
    finally { setSubmitting(false) }
  }
  return <AuthFrame title="Comece por aqui" subtitle="Crie sua conta e coloque suas finanças em ordem.">
    <form className="form-stack" onSubmit={handleSubmit(submit)} noValidate>
      <label className="field"><span>Nome</span><input autoComplete="name" placeholder="Como podemos chamar você?" {...register('name')} />{errors.name && <small className="field-error">{errors.name.message}</small>}</label>
      <label className="field"><span>E-mail</span><input type="email" autoComplete="email" placeholder="voce@email.com" {...register('email')} />{errors.email && <small className="field-error">{errors.email.message}</small>}</label>
      <label className="field"><span>Senha</span><input type="password" autoComplete="new-password" placeholder="Mínimo de 8 caracteres" {...register('password')} />{errors.password && <small className="field-error">{errors.password.message}</small>}</label>
      {message && <div className="alert alert-error" role="alert">{message}</div>}
      <button className="button button-primary button-wide" disabled={submitting}>{submitting ? 'Criando conta…' : 'Criar conta'}<ArrowRight size={17} /></button>
    </form>
    <p className="auth-switch">Já tem uma conta? <Link to="/login">Entrar</Link></p>
  </AuthFrame>
}
