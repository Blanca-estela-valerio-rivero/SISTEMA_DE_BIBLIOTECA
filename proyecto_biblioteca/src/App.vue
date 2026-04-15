<template>
  <div class="page-shell">
    <section class="hero">
      <div>
        <p class="eyebrow">Sistema de bibliotecas</p>
        <h1>Gestión de libros </h1>
        <p class="hero-copy">
          Registra, actualiza y elimina libros desde una sola pantalla. El catálogo se
          guarda en el navegador para que no se pierda al recargar.
        </p>
      </div>
      <div class="hero-card">
        <span class="hero-card-label">Registros activos</span>
        <strong>{{ books.length }}</strong>
        <small>Libros guardados en el catálogo</small>
      </div>
    </section>

    <main class="content-grid">
      <section class="panel form-panel">
        <div class="panel-header">
          <div>
            <h2>{{ isEditing ? 'Actualizar libro' : 'Registrar libro' }}</h2>
            <p>Completa los datos obligatorios para guardar el registro.</p>
          </div>
          <span class="status-pill" :class="isEditing ? 'editing' : 'new'">
            {{ isEditing ? 'Modo edición' : 'Nuevo registro' }}
          </span>
        </div>

        <form class="book-form" @submit.prevent="saveBook">
          <div class="field-grid">
            <label class="field">
              <span>Nombre del libro</span>
              <input v-model.trim="form.nombreLibro" type="text" placeholder="Ej. Cien años de soledad" />
              <small v-if="errors.nombreLibro">{{ errors.nombreLibro }}</small>
            </label>

            <label class="field">
              <span>Nombre del autor</span>
              <input v-model.trim="form.autor" type="text" placeholder="Ej. Gabriel García Márquez" />
              <small v-if="errors.autor">{{ errors.autor }}</small>
            </label>

            <label class="field">
              <span>Género</span>
              <input v-model.trim="form.genero" type="text" placeholder="Ej. Novela" />
              <small v-if="errors.genero">{{ errors.genero }}</small>
            </label>

            <label class="field">
              <span>Cantidad de ejemplares</span>
              <input v-model.number="form.cantidad" type="number" min="1" step="1" placeholder="Ej. 8" />
              <small v-if="errors.cantidad">{{ errors.cantidad }}</small>
            </label>

            <label class="field field-full">
              <span>Año de publicación</span>
              <input v-model.number="form.anioPublicacion" type="number" min="0" step="1" placeholder="Ej. 1967" />
              <small v-if="errors.anioPublicacion">{{ errors.anioPublicacion }}</small>
            </label>
          </div>

          <div v-if="feedback.message" class="feedback" :class="feedback.type">
            {{ feedback.message }}
          </div>

          <div class="form-actions">
            <button class="primary-btn" type="submit">
              {{ isEditing ? 'Actualizar libro' : 'Registrar libro' }}
            </button>
            <button class="secondary-btn" type="button" @click="cancelForm">
              Cancelar
            </button>
          </div>
        </form>
      </section>

      <section class="panel table-panel">
        <div class="panel-header compact">
          <div>
            <h2>Libros registrados</h2>
            <p>Tabla con opción para actualizar o eliminar cada libro.</p>
          </div>
        </div>

        <div v-if="books.length === 0" class="empty-state">
          <h3>No hay libros registrados todavía.</h3>
          <p>Usa el formulario para crear el primer registro.</p>
        </div>

        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Libro</th>
                <th>Autor</th>
                <th>Género</th>
                <th>Ejemplares</th>
                <th>Año</th>
                <th>Acciones</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="book in books" :key="book.id">
                <td data-label="Libro">{{ book.nombreLibro }}</td>
                <td data-label="Autor">{{ book.autor }}</td>
                <td data-label="Género">{{ book.genero }}</td>
                <td data-label="Ejemplares">{{ book.cantidad }}</td>
                <td data-label="Año">{{ book.anioPublicacion }}</td>
                <td data-label="Acciones">
                  <div class="row-actions">
                    <button type="button" class="ghost-btn" @click="editBook(book)">Actualizar</button>
                    <button type="button" class="danger-btn" @click="removeBook(book.id)">Eliminar</button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';

const STORAGE_KEY = 'biblioteca-libros-vue';

const books = ref([]);
const editingId = ref(null);

const form = reactive({
  nombreLibro: '',
  autor: '',
  genero: '',
  cantidad: '',
  anioPublicacion: '',
});

const errors = reactive({
  nombreLibro: '',
  autor: '',
  genero: '',
  cantidad: '',
  anioPublicacion: '',
});

const feedback = reactive({
  type: '',
  message: '',
});

const isEditing = computed(() => editingId.value !== null);

const clearFeedback = () => {
  feedback.type = '';
  feedback.message = '';
};

const setFeedback = (type, message) => {
  feedback.type = type;
  feedback.message = message;
};

const resetErrors = () => {
  Object.keys(errors).forEach((key) => {
    errors[key] = '';
  });
};

const clearFormFields = () => {
  form.nombreLibro = '';
  form.autor = '';
  form.genero = '';
  form.cantidad = '';
  form.anioPublicacion = '';
};

const resetForm = () => {
  clearFormFields();
  editingId.value = null;
  resetErrors();
  clearFeedback();
};

const validateForm = () => {
  resetErrors();
  let valid = true;

  if (!form.nombreLibro) {
    errors.nombreLibro = 'El nombre del libro es obligatorio.';
    valid = false;
  }

  if (!form.autor) {
    errors.autor = 'El nombre del autor es obligatorio.';
    valid = false;
  }

  if (!form.genero) {
    errors.genero = 'El género es obligatorio.';
    valid = false;
  }

  if (!Number.isInteger(Number(form.cantidad)) || Number(form.cantidad) <= 0) {
    errors.cantidad = 'La cantidad debe ser un número entero mayor que 0.';
    valid = false;
  }

  if (!Number.isInteger(Number(form.anioPublicacion)) || Number(form.anioPublicacion) <= 0) {
    errors.anioPublicacion = 'El año de publicación debe ser un número válido.';
    valid = false;
  }

  return valid;
};

const saveBook = () => {
  if (!validateForm()) {
    setFeedback('error', 'Revisa los campos marcados antes de continuar.');
    return;
  }

  const payload = {
    id: editingId.value ?? Date.now(),
    nombreLibro: form.nombreLibro,
    autor: form.autor,
    genero: form.genero,
    cantidad: Number(form.cantidad),
    anioPublicacion: Number(form.anioPublicacion),
  };

  if (isEditing.value) {
    books.value = books.value.map((book) => (book.id === editingId.value ? payload : book));
    setFeedback('success', 'El libro se actualizó correctamente.');
  } else {
    books.value = [...books.value, payload];
    setFeedback('success', 'El libro se registró correctamente.');
  }

  clearFormFields();
  editingId.value = null;
  resetErrors();
};

const editBook = (book) => {
  editingId.value = book.id;
  form.nombreLibro = book.nombreLibro;
  form.autor = book.autor;
  form.genero = book.genero;
  form.cantidad = book.cantidad;
  form.anioPublicacion = book.anioPublicacion;
  resetErrors();
  setFeedback('info', 'Editando un registro existente.');
};

const cancelForm = () => {
  resetForm();
  setFeedback('info', 'La operación fue cancelada.');
};

const removeBook = (id) => {
  const book = books.value.find((item) => item.id === id);

  if (!book) {
    return;
  }

  const shouldDelete = window.confirm(`¿Deseas eliminar el libro "${book.nombreLibro}"?`);

  if (!shouldDelete) {
    return;
  }

  books.value = books.value.filter((item) => item.id !== id);

  if (editingId.value === id) {
    resetForm();
  }

  setFeedback('success', 'El libro fue eliminado.');
};

onMounted(() => {
  const storedBooks = window.localStorage.getItem(STORAGE_KEY);

  if (storedBooks) {
    try {
      books.value = JSON.parse(storedBooks);
    } catch {
      books.value = [];
    }
  }
});

watch(
  books,
  (value) => {
    window.localStorage.setItem(STORAGE_KEY, JSON.stringify(value));
  },
  { deep: true }
);
</script>